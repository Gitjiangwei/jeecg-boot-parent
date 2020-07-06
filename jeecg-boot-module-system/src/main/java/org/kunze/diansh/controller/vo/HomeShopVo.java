package org.kunze.diansh.controller.vo;

import lombok.Data;
import org.kunze.diansh.entity.HomeShop;

import java.io.Serializable;

@Data
public class HomeShopVo extends HomeShop implements Serializable {

    //专区名称
    private String homgName;

    //专区占位图
    private String image;

}
