package org.kunze.diansh.entity.modelData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.kunze.diansh.entity.Spu;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpuModel implements Serializable {


    private String id; //spu Id

    private String title;// 标题

    private String subTitle;// 子标题

    private String saleable;// 是否上架


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;// 创建时间

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;// 最后修改时间

    private String updateName; //修改/创建人

    private String cName; //商品分类名称

    private String bName; //商品品牌名称

    private String image; //图片

    private String images; //图片

    private String newPrice; //优惠价格

    private String price; //价格

    private String stock; //库存

    private Long barCode; //条形码
}
