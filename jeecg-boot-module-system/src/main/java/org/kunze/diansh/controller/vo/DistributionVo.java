package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DistributionVo implements Serializable {

    //称呼
    private String call;
    //联系方式
    private String contact;
    //配送地址
    private String shippingAddress;


}
