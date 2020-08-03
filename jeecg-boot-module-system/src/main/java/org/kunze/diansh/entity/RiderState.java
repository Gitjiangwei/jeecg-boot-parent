package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RiderState implements Serializable {

    private String  ridersId;//骑手ID
    private Integer isOpen;//是否开启接单 0：关闭 1：开启
    private Integer isRemind;//是否开启接单提醒 0:关闭 1：开启
    private Integer isVibration;//是否开启震动 0:关闭 1:开启
    private String  version;//版本
    private Integer riderType;//骑手类型：1：超市 2：餐饮
}
