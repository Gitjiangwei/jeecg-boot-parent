package org.kunze.diansh.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.EmptyUtils;
import org.jeecg.common.util.RedisUtil;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.SkuMapper;
import org.kunze.diansh.mapper.SpuFeaturesMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;

    @Autowired
    private RedisUtil redisUtil;





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

                Sku sku = skuMapper.querySkuInfoById(od.getSkuId());
                if(sku.getIsFeatures().equals("1")){
                    SpuFeatures features = spuFeaturesMapper.selectFeatBySkuId(od.getSkuId());
                    Integer featureNum = Integer.parseInt(features.getFeaturesStock());
                    System.out.println("===============================特卖商品剩余数量"+featureNum);
                    if(featureNum == 0){
                        //库存数量为0
                        continue;
                    }
                    if(od.getNum()>featureNum){
                        System.out.println("================================特卖商品库存不足");
                        continue;
                    }
                    spuFeaturesMapper.updateFeatStock(features.getFeaturesId(),featureNum);
                }else{
                    //剩余的库存数量
                    Integer stockNum = stockMapper.selectStockLock(od.getSkuId());
                    System.out.println("===============================商品剩余数量"+stockNum);
                    if(stockNum == 0){
                        //库存数量为0
                        continue;
                    }
                    if(od.getNum()>stockNum){
                        System.out.println("================================库存不足");
                        continue;
                    }
                    //更新库存
                    stockMapper.updateStockNum(od.getNum(),od.getSkuId());
                }
            }
        }catch (Exception e){
            System.out.println("========================================================更新商品库存时失败！"+e.getMessage());
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
            //redisUtil.hdecr(StockType.STOCK_PREFIX,stock.getSkuId(),stockNum);
        }
        return isflag;
    }

    /**
     * 初始化库存信息
     */
    @Override
    public void initStockInfo(){
        QueryWrapper<Stock> queryWrapper = new QueryWrapper();
        List<Stock> stocks = stockMapper.selectList(queryWrapper);
        for (Stock stock:stocks) {
            //不存在redis就添加进去
            if(!redisUtil.hHasKey(StockType.STOCK_PREFIX,stock.getSkuId())){
                redisUtil.hset(StockType.STOCK_PREFIX,stock.getSkuId(),Integer.parseInt(stock.getStock()));
            }
        }
    }

    /**
     * 批量添加库存
     * @param stockList
     */
    @Override
    public void addStock(List<Stock> stockList) {
        for (Stock stock:stockList) {
            redisUtil.hset(StockType.STOCK_PREFIX,stock.getSkuId(),Integer.parseInt(stock.getStock()));
        }
    }

    /**
     * 检查库存是否充足
     * @param skuId
     * @param stockNum
     * @return
     */
    @Override
    public Boolean contrastStockBySkuId(String skuId,String stockNum){
        Boolean flag = false;
        Stock stock = stockMapper.selectById(skuId);
        if(redisUtil.hHasKey(StockType.STOCK_PREFIX,skuId)){
            Object num = redisUtil.hget(StockType.STOCK_PREFIX,stock.getSkuId());
            Boolean isGreater = NumberUtil.isGreater(new BigDecimal(num.toString()),new BigDecimal(stockNum));
            if(isGreater){
                flag = true;
            }
        }else{
            flag = true;
            System.out.println("库存不存在！");
        }
        return flag;
    }


    /**
     * 从数据库中对比库存
     * @param skuId
     * @param stockNum
     * @return
     */
    @Override
    public Boolean costStock(String skuId,Integer stockNum){
        Boolean flag = false;
        Stock stock = stockMapper.selectById(skuId);
        if(EmptyUtils.isNotEmpty(stock)){
            String num = stock.getStock();
            //库中的库存大于等于购买的库存 代表可以购买
            if(Integer.parseInt(num) >= stockNum){
                flag = true;
            }
        }else{
            System.out.println("查询库存时出现错误！");
        }
        return flag;
    }

    /**
     * 回滚库存
     * @param odList
     */
    @Override
    public void rollBackStock(List<OrderDetail> odList){
        for (OrderDetail od:odList) {
            Stock stock = new Stock();
            stock.setSkuId(od.getSkuId());
            stock.setStock(od.getNum().toString());
            this.updateStock(stock);
            //redisUtil.hincr(StockType.STOCK_PREFIX,od.getSkuId(),od.getNum());
        }
    }

    /**
     * 回滚redis库存
     * @param odList
     */
    @Override
    public void rollBackRedisStock(List<OrderDetail> odList){
        for (OrderDetail od:odList) {
            redisUtil.hincr(StockType.STOCK_PREFIX,od.getSkuId(),od.getNum());
        }
    }
}
