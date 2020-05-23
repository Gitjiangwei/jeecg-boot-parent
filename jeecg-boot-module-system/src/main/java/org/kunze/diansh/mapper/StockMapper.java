package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.Stock;

import java.util.List;

public interface StockMapper extends BaseMapper<Stock> {


    int saveStock(@Param("stocks") List<Stock> stocks);

    int updateStock(@Param("StockList")List<Stock> stockList);

    //查询库存数量 使用悲观锁
    Integer selectStockLock(@Param("skuId")String skuId);

    //减库存
    int updateStockNum(@Param("skuNum")Integer skuNum,@Param("skuId")String skuId);
}
