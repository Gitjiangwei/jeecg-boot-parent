package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DelFileUtils;
import org.kunze.diansh.controller.bo.WheelBo;
import org.kunze.diansh.controller.vo.WheelVo;
import org.kunze.diansh.entity.Wheel;
import org.kunze.diansh.mapper.WheelMapper;
import org.kunze.diansh.service.IWheelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class WheelServiceImpl extends ServiceImpl<WheelMapper, Wheel> implements IWheelService {

    @Autowired
    private WheelMapper wheelMapper;

    /***
     * 添加轮播图片
     * @param wheelBo
     * @return
     */
    @Override
    public Boolean saveWheel(WheelBo wheelBo) {
        Boolean isFlag = false;
        if(wheelBo.getWheelPort()==null||wheelBo.getWheelPort().equals("")){
            return false;
        }
        if(wheelBo.getIsFlag().equals("true")){
            wheelBo.setIsFlag("0");
        }else {
            wheelBo.setIsFlag("1");
        }
        Wheel wheel = new Wheel();
        BeanUtils.copyProperties(wheelBo,wheel);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            wheel.setUpdateName(sysUser.getRealname());
        } else {
            wheel.setUpdateName("");
        }
        wheel.setWheelId(UUID.randomUUID().toString().replace("-",""));
        int result = wheelMapper.saveWheel(wheel);
        if(result > 0){
            isFlag = true;
        }
        return isFlag;
    }

    /***
     * 查询轮播图片
     * @param wheelVo
     * @return
     */
    @Override
    public PageInfo<Wheel> queryWheel(WheelVo wheelVo,Integer pageNo,Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        Wheel wheel = new Wheel();
        BeanUtils.copyProperties(wheelVo,wheel);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            wheel.setUpdateName(sysUser.getRealname());
        } else {
            wheel.setUpdateName("");
        }
        List<Wheel> wheelList = wheelMapper.qeryWheel(wheel);
        return new PageInfo<Wheel>(wheelList);
    }

    /***
     * 修改轮播图片
     * @param wheelBo
     * @return
     */
    @Override
    public Boolean updateWheel(WheelBo wheelBo) {
        Boolean isFlag = false;
        if(wheelBo.getWheelId()!=null && !wheelBo.getWheelId().equals("")){
            Wheel wheel = new Wheel();
            if(wheelBo.getIsFlag().equals("true")){
                wheelBo.setIsFlag("0");
            }else {
                wheelBo.setIsFlag("1");
            }
            BeanUtils.copyProperties(wheelBo,wheel);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (sysUser != null) {
                wheel.setUpdateName(sysUser.getRealname());
            } else {
                wheel.setUpdateName("");
            }
            String oldImages = wheelMapper.queryImages(wheelBo.getWheelId());
            if(!StringUtils.isEmpty(oldImages)){
                DelFileUtils.delFile(oldImages);
            }
            int result = wheelMapper.updateWheel(wheel);
            if(result > 0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /***
     * 单个删除
     * @param wheelId
     * @return
     */
    @Override
    public Boolean delWheel(String wheelId) {
        Boolean flag = false;
        if(wheelId!=null && !wheelId.equals("")){
            List<String> list = new ArrayList<String>();
            list.add(wheelId);
            int result = wheelMapper.delWheels(list);
            if(result>0){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 批量删除
     *
     * @param wheelIds
     * @return
     */
    @Override
    public Boolean delWheels(String wheelIds) {
        Boolean flag = false;
        if(wheelIds != null && !wheelIds.equals("")){
            char a = wheelIds.charAt(wheelIds.length()-1);
            if(a == ','){
                wheelIds = wheelIds.substring(0,wheelIds.length()-1);
            }
            if(wheelIds == null || wheelIds.equals("")){
                return false;
            }
            List<String> list = new ArrayList<String>();
            if(wheelIds.contains(",")){
                list = new ArrayList<String>(Arrays.asList(wheelIds.split(",")));
            }else{
                list.add(wheelIds);
            }
            int result = wheelMapper.delWheels(list);
            if(result>0){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 批量修改启用还是不启用1
     *
     * @param
     * @return
     */
    @Override
    public Boolean updateIsFlag(String isFlag,String wheelIds) {
        Boolean isflag = false;
        if(wheelIds != null && !wheelIds.equals("")){
            char a = wheelIds.charAt(wheelIds.length()-1);
            if(a == ','){
                wheelIds = wheelIds.substring(0,wheelIds.length()-1);
            }
            if(wheelIds==null || wheelIds.equals("")){
                return false;
            }
            if(isFlag == null && isFlag.equals("")){
                return false;
            }
            List<String> list = new ArrayList<String>();
            if(wheelIds.contains(",")){
                list = new ArrayList<String>(Arrays.asList(wheelIds.split(",")));
            }else {
                list.add(wheelIds);
            }
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String updateName = "";
            if (sysUser != null) {
               updateName = sysUser.getRealname();
            }
            int result = wheelMapper.updateIsFlag(isFlag,updateName,list);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }

    /***
     * 后台查询轮播图
     * @param wheelBo
     * @return
     */
    @Override
    public PageInfo<Wheel> qeryWheelbackstage(WheelBo wheelBo, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        Wheel wheel = new Wheel();
        BeanUtils.copyProperties(wheelBo,wheel);
        List<Wheel> wheels = wheelMapper.qeryWheelbackstage(wheel);
        PageInfo<Wheel> pageInfo = new PageInfo<Wheel>(wheels);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /**
     * 根据轮播图id查询超市id
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectByShopId(String id) {
        if(id == null || id.equals("")){
            return null;
        }else {
            List<String> stringList = wheelMapper.selectByShopId(id);
            return stringList;
        }
    }
}
