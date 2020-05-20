package org.jeecg.modules.message.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserShop implements Serializable {

    /**超市和用户中间表ID*/
    private String id;

    /**超市Id*/
    private String shopId;

    /**用户Id*/
    private String userId;

    /**超市中的角色*/
    private String status;

    /**状态*/
    private String isOnline;
}
