package org.kunze.diansh.entity.modelData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BrandModel implements Serializable {

    //品牌id
    private String bId;

    //品牌名称
    private String bName;

    //品牌图片
    private String image;

    //品牌首字母
    private String letter;

    //商品分类名称
    private String kName;

    //商品分类id
    private String kId;

    //创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //操作人
    private String updateName;

}
