package org.kunze.diansh.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.boot.web.reactive.context.StandardReactiveWebEnvironment;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpuVo implements Serializable {

    private String id; //spu Id

    private String title;// 标题

    private String subTitle;// 子标题

    private String saleable;// 是否上架

    private String cid3; //商品分类名称

    private String brandId; //商品品牌名称

    private String image; //商品图片

    private String images; //商品多图片

    private String shopId; //超市id

    private Long barCode; //条形码

}
