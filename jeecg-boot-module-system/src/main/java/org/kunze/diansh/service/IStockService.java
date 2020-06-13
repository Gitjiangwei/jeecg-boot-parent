package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Stock;

import java.util.List;

public interface IStockService extends IService<Stock> {

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
}
