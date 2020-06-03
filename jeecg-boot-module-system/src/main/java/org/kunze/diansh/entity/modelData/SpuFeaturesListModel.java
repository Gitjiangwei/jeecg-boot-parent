package org.kunze.diansh.entity.modelData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpuFeaturesListModel implements Serializable {

    /**特卖商品ID*/
    private String featuresId;

    /**特卖商品名称*/
    private String title;

    /**规格*/
    private String ownSpec;

    /**特卖商品价格*/
    private String featuresPrice;

    /**特卖商品库存*/
    private String featuresStock;

    /**特卖商品状态 0：未开始 1：售卖中 2：已结束*/
    private String featuresStatus;

    /**特卖时间*/
    private String specialTime;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**创建人*/
    private String createName;

    /**权重*/
    private String featuresWeight;

    /**备注*/
    private String remarks;

    /**图片*/
    private String image;
}
