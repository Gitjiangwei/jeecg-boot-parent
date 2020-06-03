package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpuFeatures implements Serializable {

    /**商品类别id*/
    private String featuresId;

    /**商品ID*/
    private String spuId;

    /**商品类别 1、时令水果，2、新鲜蔬菜*/
    private String featuresFlag;

    /**创建时间*/
    private String createTime;

    /**创建人*/
    private String createName;

    /**是否删除 0、未删除 1、已删除*/
    private String isFlag;

    /**权重 数值越低，排序越靠前*/
    private String featuresWeight;

    /**备注*/
    private String remarks;

    /**超市id*/
    private String shopId;

    /**热卖开始时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialstartTime;


    /**热卖结束时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialendTime;

    /**SkuId*/
    private String skuId;

    /**热卖价格*/
    private String featuresPrice;

    /**热卖库存*/
    private String featuresStock;

    /**状态：0：未开始 1：售卖中 2：已接收*/
    private String featuresStatus;
}
