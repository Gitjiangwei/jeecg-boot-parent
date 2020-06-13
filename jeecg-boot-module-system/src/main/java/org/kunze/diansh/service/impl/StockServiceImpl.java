package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.OrderDetail;
import org.kunze.diansh.entity.Stock;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 更新库存数量
     * @param orderId 订单id
     */
    @Transactional
    @Override
    public void updateStockNum(String orderId) {
        try{
            //查询商品集合
            List<OrderDetail> odList = orderMapper.selectOrderDetailById(orderId);
            if(EmptyUtils.isEmpty(odList)){
                log.error("更新库存时未查询到商品集合：odList!");
                return;
            }
            for (OrderDetail od:odList) {
                //剩余的库存数量
                Integer stockNum = stockMapper.selectStockLock(od.getSkuId());
                if(stockNum == 0){
                    //库存数量为0
                    continue;
                }
                //更新库存
                stockMapper.updateStockNum(od.getNum(),od.getSkuId());
            }
        }catch (Exception e){
            e.printStackTrace();
            //手动抛出异常 事务回滚！
            throw new RuntimeException();
        }
    }

    /**
     * 修改库存
     *
     * @param stock
     * @return
     */
    @Override
    public Boolean updateStock(Stock stock) {
        Boolean isflag = false;
        if(!StringUtils.isEmpty(stock.getSkuId())){
            Integer stockNum = 0;
            if(stock.getStock()!=null&&!stock.getStock().equals("")){
                stockNum = Integer.valueOf(stock.getStock());
            }
            int result = stockMapper.updateStockJia(stock,stockNum);
            if(result>0){
                isflag = true;
            }
        }
        return isflag;
    }
}
