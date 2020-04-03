package org.kunze.diansh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存表
 */
@Data
public class Stock implements Serializable {

    //sku Id
    private String skuId;

    //可秒杀库存
    private String seckillStock;

    //秒杀总数量
    private String seckillTotal;

    //库存数量
    private String stock;
}
