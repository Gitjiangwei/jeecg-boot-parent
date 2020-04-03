package org.kunze.diansh.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpuBrandVo implements Serializable {

    //品牌Id
    private String key;

    //品牌名称
    private String brandName;

}
