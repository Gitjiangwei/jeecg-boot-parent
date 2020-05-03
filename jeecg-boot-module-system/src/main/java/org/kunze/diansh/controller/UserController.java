package org.kunze.diansh.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.AddressException;
import org.jeecg.common.system.vo.LoginUser;
import org.kunze.diansh.entity.Address;
import org.kunze.diansh.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Api(tags = "用户模块")
@RestController
@RequestMapping(value = "/kunze/user")
/**
 * 用户相关操作类 例如 用户信息 用户地址
 */
public class UserController {

    @Autowired
    private IAddressService addressService;

    @ApiOperation("添加用户地址信息")
    @AutoLog("添加用户地址信息")
    @PostMapping(value = "/insertAddress")
    public Result<Object> insertAddress(@RequestParam(name = "address") String address){
        Result<Object> resultList = new Result<Object>();
        Address addressObject = JSON.parseObject(address,Address.class);
        if(!addressValidate(addressObject)){
            resultList.error500("参数丢失");
            return resultList;
        }
        //获取登录对象
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        if(null == sysUser){
//            //验证是否登录
//            resultList.setSuccess(false);
//            resultList.setMessage("未登录，请登录后添加地址！");
//            return resultList;
//        }
        try {
            addressService.insertAddress(addressObject);
        } catch (AddressException e) {
            resultList.setSuccess(false);
            resultList.setMessage("创建地址时出现错误！");
            e.printStackTrace();
        }
        return resultList;
    }

    @ApiOperation("查询用户地址信息")
    @AutoLog("添加用户地址信息")
    @PostMapping(value = "/selectAddress")
    public Result<List<Address>> selectAddress(@RequestParam(name = "userID") String userID) {
        Result<List<Address>> result = new Result<List<Address>>();
        List<Address> addressList = addressService.selectAddress(userID);
        result.setSuccess(true);
        result.setResult(addressList);
        return result;
    }

    @ApiOperation("修改用户地址信息")
    @AutoLog("修改用户地址信息")
    @PostMapping(value = "/updateAddress")
    public Result<T> updateAddress(@RequestParam(name = "address") String address) {
        Result<T> result = new Result<T>();
        Address addressObject = JSON.parseObject(address,Address.class);
        if(!addressValidate(addressObject)){
            result.error500("参数丢失");
            return result;
        }
        addressService.updateAddress(addressObject);
        return result;
    }

    @ApiOperation("删除地址")
    @AutoLog("删除地址")
    @PostMapping(value = "/deleteAddress")
    public Result<T> deleteAddress(@RequestParam(name = "id") String id) {
        Result<T> result = new Result<T>();
        if (null == id || "".equals(id)) {
            result.error500("参数为空！");
        }
        Boolean isDel = addressService.deleteAddress(id);
        if (isDel) {
            result.setSuccess(true);
            result.setMessage("删除成功！");
        } else {
            result.setSuccess(false);
            result.setMessage("删除失败！");
        }
        return result;
    }

    @ApiOperation("设置默认地址")
    @AutoLog("设置默认地址")
    @PostMapping(value = "/updateDefaultByID")
    public Result<T> updateDefaultByID(@RequestParam(name = "id") String id, @RequestParam(name = "userID") String userID){
        Result<T> result = new Result<T>();
        if (null == id || "".equals(id)) {
            result.error500("参数为空！");
        }
        if (null == userID || "".equals(userID)) {
            result.error500("参数为空！");
        }
        try {
            addressService.updateDefaultByID(id, userID);
        } catch (AddressException e) {
            result.setSuccess(false);
            result.setMessage("设置默认地址时出现错误！");
            e.printStackTrace();
        }
        return result;
    }



    //验证地址格式
    private boolean addressValidate(Address address){

        if(null == address.getUserId() && !"".equals(address.getUserId()) ){
            return false;
        }
        //收货人
        if(null == address.getConsignee() && !"".equals(address.getConsignee()) ){
            return false;
        }
        //性别
        if(address.getConsigneeSex() < 0 || address.getConsigneeSex() >1 ){
            return false;
        }
        //省
        if(null == address.getProvince() && !"".equals(address.getProvince()) ){
            return false;
        }
        //市
        if(null == address.getCity() && !"".equals(address.getCity()) ){
            return false;
        }
        //区/县
        if(null == address.getCounty() && !"".equals(address.getCounty()) ){
            return false;
        }
        //地址类型
        if(null == address.getAddType() && !"".equals(address.getAddType()) ){
            return false;
        }
        //默认地址
        if(null == address.getIsDefault() && !"".equals(address.getIsDefault()) ){
            return false;
        }
        return true;
    }

}
