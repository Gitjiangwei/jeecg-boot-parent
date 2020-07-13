package org.kunze.diansh.entity.modelData;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class KzSpuTemplateModel implements Serializable {

    private String id; //spu Id

    private String cid1;

    private String cid2;

    private String cid3;

    private String title;// 标题

    private String subTitle;// 子标题

    private Integer saleable;// 是否上架


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;// 创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;// 最后修改时间

    private String updateName; //修改/创建人

    private String image; //图片

    private String images; //图片

    private String shopId;

    private Integer isFlag;// 是否有效，逻辑删除用

    private String brandId;



}
