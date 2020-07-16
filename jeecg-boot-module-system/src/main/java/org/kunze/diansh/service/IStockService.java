package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.OrderDetail;
import org.kunze.diansh.entity.Stock;

import java.util.List;

public interface IStockService extends IService<Stock> {

    //库存常量池
    public static class StockType{
        public static final String STOCK_PREFIX = "STOCK";
    }

    /**
     * 更新库存数量
     * @param orderId 订单id
     */
    void updateStockNum(String orderId);


    /**
     * 修改库存
     * @param stock
     * @return
     */
    Boolean updateStock(Stock stock);

    /**
     * 初始化库存
     */
    void initStockInfo();

    /**
     * 批量添加库存
     * @param stockList
     */
    void addStock(List<Stock> stockList);

    /**
     * 检查库存是否充足
     * @param skuId
     * @param stockNum
     * @return
     */
    Boolean contrastStockBySkuId(String skuId,String stockNum);

    /**
     * 回滚库存
     * @param odList
     */
    void rollBackStock(List<OrderDetail> odList);
}
