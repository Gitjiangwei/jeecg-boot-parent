package org.jeecg.modules.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.message.entity.SysUserShop;
import org.jeecg.modules.message.mapper.SysUserShopMapper;
import org.jeecg.modules.message.service.ISysUserShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SysUserShopServiceImpl extends ServiceImpl<SysUserShopMapper,SysUserShop> implements ISysUserShopService {

    @Autowired
    private SysUserShopMapper sysUserShopMapper;


    /**
     * 添加用户和超市中间表
     *
     * @param sysUserShop
     * @return
     */
    @Override
    public Boolean saveSysUserShop(SysUserShop sysUserShop) {
        Boolean flag = false;
        sysUserShop.setId(UUID.randomUUID().toString().replace("-",""));
        int result = sysUserShopMapper.saveSysUserShop(sysUserShop);
        if(result>0){
            flag = true;
        }
        return flag;
    }

    /**
     * 添加用户和超市中间表
     *
     * @param sysUserShop
     * @return
     */
    @Override
    public Boolean updateSysUserShop(SysUserShop sysUserShop) {
        Boolean flag = false;
        if(sysUserShop.getUserId()!=null && !sysUserShop.getUserId().equals("")){
            String shopId = sysUserShopMapper.selectByUserId(sysUserShop.getUserId());
            if(shopId!=null) {
                int result = sysUserShopMapper.updateSysUserShop(sysUserShop);
                if (result > 0) {
                    flag = true;
                }
            }else {
                flag = this.saveSysUserShop(sysUserShop);
            }
        }
        return flag;
    }

    /**
     * 根据用户ID删除中间表
     *
     * @param userId
     * @return
     */
    @Override
    public Boolean delSysUserShop(String userId) {
        Boolean flag = false;
        if(userId != null && !userId.equals("")){
            int result = sysUserShopMapper.delSysUserShop(userId);
            if(result > 0){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 根据用户ID获取所属超市ID
     *
     * @param userId
     * @return
     */
    @Override
    public String selectByUserId(String userId) {
        String shopId = null;
        if(userId !=null && !userId.equals("")){
            shopId = sysUserShopMapper.selectByUserId(userId);
        }
        return shopId;
    }
}
