package org.kunze.diansh.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存表
 */
@Data
@TableName(value = "kz_stock")
public class Stock implements Serializable {

    //sku Id
    @TableId("sku_id")
    private String skuId;

    //可秒杀库存
    private String seckillStock;

    //秒杀总数量
    private String seckillTotal;

    //库存数量
    private String stock;
}
