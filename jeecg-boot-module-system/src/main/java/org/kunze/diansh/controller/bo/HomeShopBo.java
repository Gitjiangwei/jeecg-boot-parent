package org.kunze.diansh.controller.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HomeShopBo implements Serializable {

    //超市Id
    private String shopId;

    //专区名称
    private String homgName;
}
