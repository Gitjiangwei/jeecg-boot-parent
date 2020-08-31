package org.kunze.diansh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SPU表
 */
@Data
public class Spu implements Serializable {

    private String id;

    private String brandId;

    private String cid1;// 1级类目

    private String cid2;// 2级类目

    private String cid3;// 3级类目

    private String title;// 标题

    private String subTitle;// 子标题

    private String saleable;// 是否上架

    private String isFlag;// 是否有效，逻辑删除用

    private String images; //商品多图片

    private String image; //商品单图片

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;// 创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;// 最后修改时间

    private String updateName; //修改/创建人

    private String shopId; //超市Id

    private Long barCode; //条形码
}
