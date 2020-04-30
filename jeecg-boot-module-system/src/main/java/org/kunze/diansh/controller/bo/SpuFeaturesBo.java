package org.kunze.diansh.controller.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpuFeaturesBo implements Serializable {
    //类别id
    private String featuresId;

    //商品ID
    private String spuId;

    //商品类别 1、限时特卖
    private String featuresFlag;

    //权重 数值越低，排序越靠前
    private String featuresWeight;

    //备注
    private String remarks;

    //超市id
    private String shopId;

    /**热卖开始时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialstartTime;

    /**热卖结束时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialendTime;
}
