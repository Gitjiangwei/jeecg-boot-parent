package org.jeecg.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.message.entity.SysUserShop;

public interface ISysUserShopService extends IService<SysUserShop> {

    /**
     * 添加用户和超市中间表
     * @param sysUserShop
     * @return
     */
    Boolean saveSysUserShop(SysUserShop sysUserShop);


    /**
     * 添加用户和超市中间表
     * @param sysUserShop
     * @return
     */
    Boolean updateSysUserShop(SysUserShop sysUserShop);


    /**
     * 根据用户ID删除中间表
     * @param userId
     * @return
     */
    Boolean delSysUserShop(String userId);


    /**
     * 根据用户ID获取所属超市ID
     * @param userId
     * @return
     */
    String selectByUserId(String userId);
}
