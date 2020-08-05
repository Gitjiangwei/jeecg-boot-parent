package org.kunze.diansh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Date;


@Data
@TableName("kz_hotel_sku")
public class HotelSku {

    @TableId(value="id",type= IdType.AUTO)
    private Integer id; //主键id
    @NotBlank(message = "商品标题不能为空！")
    private String title; //商品标题
    @NotBlank(message = "商品介绍不能为空！")
    @TableField(value = "sku_info")
    private String skuInfo; //商品介绍
    private String images; //商品的图片，多个图片以‘,’分割
    @TableField(value = "new_price")
    private Integer newPrice; //当前价格，单位为分
    private Integer price; //销售价格，单位为分
    @TableField(value = "del_flag")
    private Integer delFlag; //是否有效，0无效，1有效
    @TableField(value = "create_time")
    private Date createTime; //添加时间
    @TableField(value = "last_update_time")
    private Date lastUpdateTime; //最后修改时间
    @TableField(value = "shop_id")
    private String shopId; //店铺id
    @TableField(value = "update_name")
    private String updateName; //操作人
    private Integer cid; //类别id
    private Integer saleable; //是否上架 0下架/1上架 默认为0
    //sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序
    private String ownSpec;

    //是否有效，0无效，1有效
    private String enable;

    //购买时的商品数量 只用于业务逻辑处理（计算订单价格）
    private Integer num;
}
