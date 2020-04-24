package org.kunze.diansh.entity.modelData;

import lombok.Data;

import java.util.List;

@Data
public class SpuDetailModel {

    private String spuId;//商品Id

    private String title; //商品标题

    private String subTitle; //商品副标题

    private String cid3; //商品分类id

    private String description; //商品详情介绍

    private String specifications; //商品全部规格参数数据

    private String specTemplate; //特有规格参数及可选值信息，json格式

    private String packingList; //包装清单

    private String afterService; //售后服务

    private String images; //商品多图片
}
