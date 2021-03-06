package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.common.util.tencentSms.SendSms;
import org.jeecg.common.util.tencentSms.SendSmsEnum;
import org.jeecg.modules.message.service.ISysUserShopService;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.RandImageUtil;
import org.kunze.diansh.service.IShopService;
import org.kunze.diansh.service.IWXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.shiro.session.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Slf4j
public class LoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysLogService logService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserShopService sysUserShopService;
    @Autowired
    private IWXPayService iwxPayService;
    @Autowired
    private IShopService shopService;

    @Value(value = "${check.yanzheng}")
    private Boolean isCheck;

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    // 确保每个用户一个session
    protected final Map<Object, Object> sessionMap = new HashMap<Object, Object>();


    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
        //update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

        //update-begin-author:taoyan date:20190828 for:校验验证码
        if(isCheck) {
            String captcha = sysLoginModel.getCaptcha();
            if (captcha == null) {
                result.error500("验证码无效");
                return result;
            }
            String lowerCaseCaptcha = captcha.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + sysLoginModel.getCheckKey(), "utf-8");
            Object checkCode = redisUtil.get(realKey);
            if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
                result.error500("验证码错误");
                return result;
            }
        }
        //update-end-author:taoyan date:20190828 for:校验验证码

        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        //用户登录信息
        userInfo(sysUser, result);
        sysBaseAPI.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return result;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        //用户退出逻辑
        String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        LoginUser sysUser = sysBaseAPI.getUserByName(username);
        if (sysUser != null) {
            sysBaseAPI.addLog("用户名: " + sysUser.getRealname() + ",退出成功！", CommonConstant.LOG_TYPE_1, null);
            log.info(" 用户名:  " + sysUser.getRealname() + ",退出成功！ ");
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            //调用shiro的logout
            SecurityUtils.getSubject().logout();
            return Result.ok("退出登录成功！");
        } else {
            return Result.error("Token无效!");
        }
    }

    /**
     * 获取访问量
     *
     * @return
     */
    @GetMapping("loginfo")
    public Result<JSONObject> loginfo() {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        Long totalVisitCount = logService.findTotalVisitCount();
        obj.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = logService.findTodayVisitCount(dayStart, dayEnd);
        obj.put("todayVisitCount", todayVisitCount);
        Long todayIp = logService.findTodayIp(dayStart, dayEnd);
        //update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
        obj.put("todayIp", todayIp);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取访问量
     *
     * @return
     */
    @GetMapping("visitInfo")
    public Result<List<Map<String, Object>>> visitInfo() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String, Object>> list = logService.findVisitCount(dayStart, dayEnd);
        result.setResult(oConvertUtils.toLowerCasePageList(list));
        return result;
    }


    /**
     * 登陆成功选择用户当前部门
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = user.getUsername();
        if (oConvertUtils.isEmpty(username)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            username = sysUser.getUsername();
        }
        String orgCode = user.getOrgCode();
        this.sysUserService.updateUserDepart(username, orgCode);
        SysUser sysUser = sysUserService.getUserByName(username);
        JSONObject obj = new JSONObject();
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        return result;
    }

    /**
     * 短信登录接口
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation("短信接口")
    @PostMapping(value = "/sms")
    public Result<String> sms(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String mobile = jsonObject.get("mobile").toString();
        //手机号模式 登录模式: "2"  注册模式: "1"
        String smsmode = jsonObject.get("smsmode").toString();
        log.info(mobile);
        if (oConvertUtils.isEmpty(mobile)) {
            result.setMessage("手机号不允许为空！");
            result.setSuccess(false);
            return result;
        }
        Object object = redisUtil.get(mobile);
        if (object != null) {
            result.setMessage("验证码10分钟内，仍然有效！");
            result.setSuccess(false);
            return result;
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        try {
            boolean b = false;
            //注册模板
            if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                if (sysUser != null) {
                    result.error500(" 手机号已经注册，请直接登录！");
                    sysBaseAPI.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
                    return result;
                }
                //b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
                b = SendSms.sendSms(mobile,captcha+","+"10", SendSmsEnum.REGISTER_TEMPLATE_CODE);
            } else {
                //登录模式，校验用户有效性
                SysUser sysUser = sysUserService.getUserByPhone(mobile);
                result = sysUserService.checkUserIsEffective(sysUser);
                if (!result.isSuccess()) {
                    return result;
                }

                /**
                 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
                 */
                if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
                    //登录模板
                    //b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
                    b = SendSms.sendSms(mobile, captcha+","+"10", SendSmsEnum.LOGIN_TEMPLATE_CODE);
                } else if (CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
                    //忘记密码模板
                    //b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                    b = SendSms.sendSms(mobile, captcha, SendSmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                }
            }

            if (b == false) {
                result.setMessage("短信验证码发送失败,请稍后重试");
                result.setSuccess(false);
                return result;
            }
            //验证码10分钟内有效
            redisUtil.set(mobile, captcha, 600);
            //update-begin--Author:scott  Date:20190812 for：issues#391
            //result.setResult(captcha);
            //update-end--Author:scott  Date:20190812 for：issues#391
            result.setSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
            result.error500(" 短信接口未配置，请联系管理员！");
            return result;
        }
        return result;
    }


    /**
     * 手机号登录接口
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation("手机号登录接口")
    @PostMapping("/phoneLogin")
    public Result<JSONObject> phoneLogin(@RequestBody JSONObject jsonObject) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = jsonObject.getString("mobile");

        //校验用户有效性
        SysUser sysUser = sysUserService.getUserByPhone(phone);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        String smscode = jsonObject.getString("captcha");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            result.setMessage("手机验证码错误");
            return result;
        }
        //用户信息
        userInfo(sysUser, result);
        //添加日志
        sysBaseAPI.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return result;
    }





    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {

        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        if(sessionMap.get(username)!=null) {
/*            Session session = (Session) sessionMap.get(username);
            // 移除存储的之前用户名和sesion的键值对
            sessionMap.remove(username);
            // 销毁之前session
            session.stop();*/
            //清空用户登录Token缓存
            redisUtil.del(sessionMap.get(username).toString());
            //清空用户登录Shiro权限缓存
            redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
            //清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
            redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
            // 确保每次登录之后将将用户名和session存进hashmap中
            sessionMap.put(username, CommonConstant.PREFIX_USER_TOKEN +token);
        }else {
            sessionMap.put(username, CommonConstant.PREFIX_USER_TOKEN +token);
        }
        //session.setAttribute(username,CommonConstant.PREFIX_USER_TOKEN + token);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            obj.put("multi_depart", 2);
        }
        //获取超市信息
        String shopId = sysUserShopService.selectByUserId(sysUser.getId());
        List<Map<String,Object>> shopMap = shopService.selectShopInfoById(shopId);
        obj.put("shopId",shopId);
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        if(EmptyUtils.isNotEmpty(shopMap)){
            obj.put("shopType",shopMap.get(0).get("shopType").toString());
            obj.put("area",shopMap.get(0).get("area").toString());
            obj.put("distributionModel",shopMap.get(0).get("distributionModel").toString());
        }
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取加密字符串
     *
     * @return
     */
    @GetMapping(value = "/getEncryptedString")
    public Result<Map<String, String>> getEncryptedString() {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", EncryptedString.key);
        map.put("iv", EncryptedString.iv);
        result.setResult(map);
        return result;
    }

    /**
     * 获取校验码
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/getCheckCode")
    public Result<Map<String, String>> getCheckCode() {
        Result<Map<String, String>> result = new Result<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String key = MD5Util.MD5Encode(code + System.currentTimeMillis(), "utf-8");
            redisUtil.set(key, code, 60);
            map.put("key", key);
            //update-begin-author：taoyan date:20200210 for:TASK #3391 【bug】安全问题，返回验证码不安全
            String encode = java.util.Base64.getEncoder().encodeToString(code.getBytes("UTF-8"));
            map.put("code", encode);
            //update-end-author：taoyan date:20200210 for:TASK #3391 【bug】安全问题，返回验证码不安全
            result.setResult(map);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 后台生成图形验证码
     *
     * @param response
     * @param key
     */
    @ApiOperation("获取验证码2")
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable String key) {
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * app登录
     *
     * @param sysLoginModel
     * @return
     * @throws Exception
     */
    @ApiOperation("app登录")
    @RequestMapping(value = "/mLogin", method = RequestMethod.POST)
    public Result<JSONObject> mLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result = new Result<JSONObject>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();

        //1. 校验用户是否有效
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

/*        String orgCode = sysUser.getOrgCode();
        if (oConvertUtils.isEmpty(orgCode)) {
            //如果当前用户无选择部门 查看部门关联信息
            List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
            if (departs == null || departs.size() == 0) {
                result.error500("用户暂未归属部门,不可登录!");
                return result;
            }
            orgCode = departs.get(0).getOrgCode();
            sysUser.setOrgCode(orgCode);
            this.sysUserService.updateUserDepart(username, orgCode);
        }*/
        JSONObject obj = new JSONObject();
        //用户登录信息
        obj.put("userInfo", sysUser);

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置超时时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2);
        //token 信息
        obj.put("token", token);
        result.setResult(obj);
        result.setSuccess(true);
        result.setCode(200);
        sysBaseAPI.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
        return result;
    }


    /**
     * 微信小程序用户登录信息
     *
     * @param result
     * @return
     */
    private Result<JSONObject> WxAppUserInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/2);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();

        obj.put("token", token);
        obj.put("userid", sysUser.getId());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }
    /**
     * 安卓用户登录信息
     *
     * @param result
     * @return
     */
    private Result<JSONObject> androidUserInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/2);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();

        obj.put("token", token);
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 微信小程序登录
     * @param js_code code
     * @return
     */
    @RequestMapping(value = "/wxAppAppLogin", method = RequestMethod.POST)
    public Result wxAppAppLogin(@RequestParam("code") String js_code){
        Result result = new Result();
        Map<String,Object> map = iwxPayService.getOpenId(js_code);
        if (EmptyUtils.isEmpty(map)) {
            return result.error500("向微信请求数据时出现错误！");
        }
        String openId = (String) map.get("openid");
        if (EmptyUtils.isEmpty(openId)) {
            return result.error500("获取openId失败！");
        }
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        SysUser user = sysUserService.selectSysUserById(openId);
        if(EmptyUtils.isEmpty(user)){
            //第一次登录时生成一个随机用户名
            String username = RandomStringUtils.randomAlphanumeric(8);
            SysUser sysUser = new SysUser();
            sysUser.setId(openId);
            sysUser.setUsername(username);
            sysUser.setPassword(openId);
            // 添加到数据库
            Boolean flag = sysUserService.insertWxAppInfo(sysUser);
            if(!flag){
                return result.error500("error!");
            }else{
                //记录登录信息
                WxAppUserInfo(sysUser,result);
                return result;
            }
        }
        WxAppUserInfo(user,result);
        return result;
    }

    /***
     * APP注册
     * @param jsonObject
     * @return
     */
    @ApiOperation("APP注册接口")
    @RequestMapping(value = "/appRegister",method = RequestMethod.POST)
    public Result<T> appRegister(@RequestBody JSONObject jsonObject){
        Result<T> result = new Result<T>();
        String phone = jsonObject.getString("phone");
        String smscode = jsonObject.getString("smscode");
        String password = jsonObject.getString("password");
        Object code = redisUtil.get(phone);
        if (!smscode.equals(code)) {
            result.setMessage("手机验证码错误");
            return result;
        }
        SysUser sysUser2 = sysUserService.getUserByPhone(phone);
        if (sysUser2 != null) {
            result.setMessage("该手机号已注册");
            result.setSuccess(false);
            return result;
        }
        SysUser user = new SysUser();
        try {
            user.setCreateTime(new Date());// 设置创建时间
            String salt = oConvertUtils.randomGen(8);
            String passwordEncode = PasswordUtil.encrypt(phone, password, salt);
            user.setSalt(salt);
            user.setUsername(phone);
            user.setRealname("hh"+RandomStringUtils.randomAlphanumeric(8));
            user.setPassword(passwordEncode);
            user.setEmail("");
            user.setPhone(phone);
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            user.setActivitiSync(CommonConstant.ACT_SYNC_1);
            sysUserService.addUserWithRole(user, "ee8626f80f7c2619917b6236f3a7f02b");//默认临时角色 test
            result.success("注册成功");
        } catch (Exception e) {
            result.error500("注册失败");
        }
        return  result;
    }

    //Todo 此安卓登录方法为简单的逻辑模拟 正式需要修改为短信验证码登录
    /**
     * 安卓登录
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/androidLogin",method = RequestMethod.POST)
    public Result androidLogin(@RequestBody JSONObject jsonObject){
        Result resultMap = new Result();
        String phone = jsonObject.getString("phone");
        String code = jsonObject.getString("code");


        if (EmptyUtils.isEmpty(phone)){
            return resultMap.error500("手机号丢失！");
        }
        if (!redisUtil.hasKey(code,1)){
            resultMap.error500("验证码错误！");
            return resultMap;
        }
        Object obj = redisUtil.get(code,1);
        if(!obj.toString().equals(phone)){
            resultMap.error500("手机号错误！");
            return resultMap;
        }



        SysUser sysUser = sysUserService.getUserByPhone(phone);
        //用户已存在直接返回
        if(EmptyUtils.isNotEmpty(sysUser)){
            androidUserInfo(sysUser,resultMap);
        }else {
            //用户不存在则添加到数据库
            SysUser user = sysUserService.insertAndroidUserInfo(phone);
            if(EmptyUtils.isNotEmpty(user)){
                androidUserInfo(user,resultMap);
            }else {
                resultMap.error500("写入数据库时出现错误！");
            }
        }
        return resultMap;
    }


    /**
     * 生成随机验证码
     * @return
     */
    @RequestMapping(value = "/getRandomCode",method = RequestMethod.POST)
    public Result getCharAndNumr(String phone) {
        Result result = new Result();
        if(EmptyUtils.isEmpty(phone)){
            return result.error("手机号参数丢失！");
        }
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789";
        int charLength = charStr.length();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        //写入到指定redis
        redisUtil.set(valSb.toString(),phone,1,false, (long) 60);
        result.setResult(valSb.toString());
        return result;
    }

    /**
     * 用户更改密码
     */
    @ApiOperation("APP更改密码")
    @PostMapping(value = "/appPwdChange")
    public Result<SysUser> passwordChange(@RequestBody JSONObject jsonObject) {
        String password = jsonObject.getString("password");
        String smscode = jsonObject.getString("smscode");
        String phone = jsonObject.getString("phone");
        Result<SysUser> result = new Result<SysUser>();
        if (oConvertUtils.isEmpty(password) || oConvertUtils.isEmpty(smscode) || oConvertUtils.isEmpty(phone)) {
            result.setMessage("重置密码失败！");
            result.setSuccess(false);
            return result;
        }

        SysUser sysUser = new SysUser();
        Object object = redisUtil.get(phone);
        if (null == object) {
            result.setMessage("短信验证码失效！");
            result.setSuccess(false);
            return result;
        }
        if (!smscode.equals(object)) {
            result.setMessage("短信验证码不匹配！");
            result.setSuccess(false);
            return result;
        }
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, phone).eq(SysUser::getPhone, phone));
        if (sysUser == null) {
            result.setMessage("未找到用户！");
            result.setSuccess(false);
            return result;
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setSuccess(true);
            result.setMessage("密码重置完成！");
            return result;
        }
    }

}