package org.kunze.diansh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.CommonUtil;
import org.kunze.diansh.controller.vo.InformationVo;
import org.kunze.diansh.controller.vo.SalesVo;
import org.kunze.diansh.entity.OrderRecord;
import org.kunze.diansh.entity.modelData.MonthMenuModel;
import org.kunze.diansh.entity.modelData.SalesModel;
import org.kunze.diansh.mapper.ChargeMapper;
import org.kunze.diansh.mapper.OrderRecordMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.mapper.StockMapper;
import org.kunze.diansh.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
import java.math.BigDecimal;
import java.rmi.MarshalledObject;
import java.text.DecimalFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    /**
     * 门店销售排行榜
     *
     * @return
     */
    @Override
    public PageInfo<Map<String, String>> selectStoreLeaderboard(String shopName,String more,String choiceTime,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Map<String,String>> newMapList = new ArrayList<Map<String, String>>();
        List<Map<String,Object>> oldMapList = shopMapper.selectStoreLeaderboard(shopName,more,choiceTime);
        for(int i =0;i< oldMapList.size();i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("shopId",oldMapList.get(i).get("id") == null?"":oldMapList.get(i).get("id").toString());
            map.put("shopName",oldMapList.get(i).get("shopName") == null?"":oldMapList.get(i).get("shopName").toString());
            String payment = "0";
            if(oldMapList.get(i).get("payment")!=null){
                BigDecimal ordPrice = new BigDecimal(String.valueOf(oldMapList.get(i).get("payment")));
                BigDecimal newPrice = ordPrice.divide(new BigDecimal("100"));
                payment = newPrice.setScale(2).toString();
            }
            map.put("payment",payment);
            map.put("charge",oldMapList.get(i).get("charge").toString());
            map.put("serviceCharge",serviceCharge(payment,map.get("charge")));
            String okTotal = oldMapList.get(i).get("okTotal")==null?"0":oldMapList.get(i).get("okTotal").toString();
            String okPayment =oldMapList.get(i).get("okPayment")==null?"0":CalculationUtil.FractionalConversion(oldMapList.get(i).get("okPayment").toString());
            String refundTotal = oldMapList.get(i).get("refundTotal")==null?"0":oldMapList.get(i).get("refundTotal").toString();
            String refundPayment =oldMapList.get(i).get("refundPayment")==null?"0":CalculationUtil.FractionalConversion(oldMapList.get(i).get("refundPayment").toString());
            map.put("okPayment",okTotal+"单"+okPayment+"元");
            //map.put("okTotal",);
            map.put("refundPayment",refundTotal+"单"+refundPayment+"元");
            //map.put("refundTotal",);
            newMapList.add(map);
        }
        PageInfo<Map<String,String>> mapPageInfo = new PageInfo<Map<String, String>>(newMapList);
        mapPageInfo.setTotal(page.getTotal());
        return mapPageInfo;
    }

    /**
     * 手续费
     * @param payMent 商家利润
     * @param charge 手续费率
     * jw
     * @return
     */
    private String serviceCharge(String payMent,String charge){
        String chargeService = "0";
        if (payMent.equals("0")){
            return chargeService;
        }
        if(!charge.equals("0")){
            charge = new BigDecimal(charge).divide(new BigDecimal("100")).toString();
            chargeService = new BigDecimal(payMent).multiply(new BigDecimal(charge)).setScale(2, BigDecimal.ROUND_UP).toString();
        }
        return chargeService;
    }
    /***
     * 门店销售统计图
     * @param shopId
     * @param year
     * @return
     */
    @Override
    public List<String> selectStoreByShop(String shopId, String year) {
        List<String> stringList = new ArrayList<String>();
        if(shopId == null || shopId.equals("")){
            List<Map<String,Object>> mapList = shopMapper.selectStoreLeaderboard(null,"0","0");
            shopId = mapList.get(0).get("id") == null?"":mapList.get(0).get("id").toString();
        }
        if(year == null || year.equals("")){
            Calendar date = Calendar.getInstance();
            year = String.valueOf(date.get(Calendar.YEAR));
        }
        MonthMenuModel monthMenuModel = shopMapper.selectStoreByShop(shopId,year);
        if(monthMenuModel == null){
            for(int i =0;i<12;i++){
                stringList.add("0");
            }
        }else {
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getJanuary()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getFebruary()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getMarch()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getApril()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getMay()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getJune()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getJuly()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getAugust()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getSeptember()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getOctober()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getNovember()));
            stringList.add(CalculationUtil.FractionalConversion(monthMenuModel.getDecember()));
        }

        return stringList;
    }

    /***
     * 查询总订单量和当前完成的订单量
     * @param shopId
     * @return
     */
    @Override
    public Map<String, String> selectOrderLeader(String shopId) {
        return shopMapper.selectOrderLeader(shopId);
    }

    /***
     * 查询近10天的订单量
     * @param shopId
     * @return
     */
    @Override
    public List<Map<String, String>> selectOrderLeaders(String shopId) {
        return shopMapper.selectOrderLeaders(shopId);
    }

    /**
     * 查询销售额
     *
     * @param shopId
     * @return
     */
    @Override
    public SalesVo selectSales(String shopId) {
        SalesVo salesVo = new SalesVo();
        SalesModel salesModel = shopMapper.selectSales(shopId);
        DecimalFormat df1 = new DecimalFormat("0.00");
        //1、计算周同比
        BigDecimal lastWeek = new BigDecimal(salesModel.getLastWeek()); //上周销售额
        BigDecimal week = new BigDecimal(salesModel.getWeek());//本周销售额
        if(lastWeek.intValue() == 0){
            if(week.intValue()!=0){
                salesVo.setOnWeek("100%");
            }else {
                salesVo.setOnWeek("0%");
            }
        }else {
            BigDecimal onWeek = week.subtract(lastWeek); //周同比
            BigDecimal onWeek1 = onWeek.divide(lastWeek, 4, ROUND_HALF_UP);
            BigDecimal onWeek2 = onWeek1.multiply(new BigDecimal("100"));

            salesVo.setOnWeek(df1.format(onWeek2) + "%");
        }
        //2、计算日同比
        BigDecimal yestToDay = new BigDecimal(salesModel.getYestToday());//前一天的销售额
        BigDecimal toDay = new BigDecimal(salesModel.getToday());//当天销售额
        if(yestToDay.intValue() == 0){
            if(toDay.intValue() != 0){
                salesVo.setOnDay("100%");
            }else {
                salesVo.setOnDay("0%");
            }
        }else {
            BigDecimal onToDay = toDay.subtract(yestToDay);//日同比
            BigDecimal onToDay1 = onToDay.divide(yestToDay,4,ROUND_HALF_UP);
            BigDecimal onToDay2 = onToDay1.multiply(new BigDecimal("100"));
            salesVo.setOnDay(df1.format(onToDay2)+"%");
        }
        //3、总销售额
        salesVo.setTotal(CalculationUtil.FractionalConversion(salesModel.getTotal()));
        //4、当天销售额
        salesVo.setToDays(CalculationUtil.FractionalConversion(salesModel.getToday()));
        return salesVo;
    }

    /**
     * 查询平台数据统计
     *
     * @param shopId
     * @return
     */
    @Override
    public InformationVo selectInfo(String shopId) {
        InformationVo informationVo = new InformationVo();
        if(shopId!=null && !shopId.equals("")) {
            Map<String,String> mapList = shopMapper.selectMonthMoney(shopId); //月交易额
            Object oldMoney = mapList.get("payment"); //月交易额(包含退款和配送费)
            //Object postFree = mapList.get("postFree"); //月配送费
            Object okPayment = mapList.get("okPayment"); // 月交易成功交易额
            Object okTotal = mapList.get("okTotal"); //月交易成功订单数
            Object refundPayment = mapList.get("refundPayment"); //月订单退款额度
            Object refundTotal = mapList.get("refundTotal");// 月退款订单数
            informationVo.setMoneyMoney(CalculationUtil.FractionalConversion(oldMoney.toString()));
            //informationVo.setMoneyPostfree(CalculationUtil.FractionalConversion(postFree.toString()));
            informationVo.setTotalMoney(informationVo.getMoneyMoney());
            informationVo.setOkPayment(CalculationUtil.FractionalConversion(okPayment.toString()));
            informationVo.setOkTotal(okTotal.toString());
            informationVo.setRefundPayment(CalculationUtil.FractionalConversion(refundPayment.toString()));
            informationVo.setRefundTotal(refundTotal.toString());
            //计算手续费
            Map<String,String> map = chargeMapper.selectCharge(shopId);
            String serviceCharge = "0";
            if(map != null) {
                serviceCharge = map.get("service_charge") == null ? "0" : map.get("service_charge");
            }
            if(!serviceCharge.equals("0")){
                informationVo.setCharge(serviceCharge);
                serviceCharge = new BigDecimal(serviceCharge).divide(new BigDecimal("100")).toString();
                informationVo.setChargeTotal(new BigDecimal(informationVo.getTotalMoney()).multiply(new BigDecimal(serviceCharge)).setScale(2, BigDecimal.ROUND_UP).toString());
                informationVo.setTotalMoney(new BigDecimal(informationVo.getTotalMoney()).subtract(new BigDecimal(informationVo.getChargeTotal())).subtract(new BigDecimal(informationVo.getRefundPayment())).toString());
            }else {
                informationVo.setCharge("0");
                informationVo.setChargeTotal("0");
            }
            Map<String,String> toDayMap = shopMapper.selectToDayMoney(shopId); //当日交易额
            Object toDayMoney = toDayMap.get("payment"); //当日交易额
            Object toDayOkPayment = toDayMap.get("okPayment"); //当日成功交易
            Object toDayokTotal = toDayMap.get("okTotal"); //当日交易成功的订单数
            Object toDayRefundPayment = toDayMap.get("refundPayment"); //当日退款金额
            Object toDayRefundTotal = toDayMap.get("refundTotal");// 当日退款订单数
            informationVo.setToDayMoney(CalculationUtil.FractionalConversion(toDayMoney.toString()));
            informationVo.setToDayOkPayment(CalculationUtil.FractionalConversion(toDayOkPayment.toString()));
            informationVo.setToDayokTotal(toDayokTotal.toString());
            informationVo.setToDayRefundPayment(CalculationUtil.FractionalConversion(toDayRefundPayment.toString()));
            informationVo.setToDayRefundTotal(toDayRefundTotal.toString());
            //计算当日的手续费
            if(!serviceCharge.equals("0")){
                //serviceCharge = new BigDecimal(serviceCharge).divide(new BigDecimal("100")).toString();
                informationVo.setToDayChargeTotal(new BigDecimal(informationVo.getToDayMoney()).multiply(new BigDecimal(serviceCharge)).setScale(2, BigDecimal.ROUND_UP).toString());
                informationVo.setToDayTotalPrice(new BigDecimal(informationVo.getToDayMoney()).subtract(new BigDecimal(informationVo.getToDayChargeTotal())).subtract(new BigDecimal(informationVo.getToDayRefundPayment())).toString());
            }else {
                informationVo.setToDayChargeTotal("0");
                informationVo.setToDayTotalPrice(new BigDecimal(informationVo.getToDayMoney()).subtract(new BigDecimal(informationVo.getToDayRefundPayment())).toString());
            }
            informationVo.setOrderNum(shopMapper.selectTotalOrder(shopId));
            informationVo.setSpuNum(shopMapper.selectTotalSpuNum(shopId));
        }
        return informationVo;
    }


    /***
     * 订单统计
     * @param shopId
     * @return
     */
    @Override
    public Map<String, String> selectOrderstatistics(String shopId) {
        return shopMapper.selectOrderstatistics(shopId);
    }

    /***
     * 库存统计
     * @param shopId
     * @return
     */
    @Override
    public Map<String, String> selectWarehouseStatistics(String shopId) {
        return shopMapper.selectWarehouseStatistics(shopId);
    }

    /**
     * 查询近7天的成交量
     *
     * @param shopId
     * @return
     */
    @Override
    public List<Map<String, String>> selectSevenDeal(String shopId) {
        return shopMapper.selectSevenDeal(shopId);
    }

    /**
     * 查询库存
     *
     * @param shopId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Map<String, Object>> selectStock(String shopId,String title,String enable, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<Map<String,Object>> mapList = stockMapper.selectStock(shopId,title,enable);
        List<Map<String,Object>> newMapList = new ArrayList<Map<String, Object>>();
        for(Map<String,Object> item:mapList) {
            Map<String, Object> map = item;
            map.put("newPrice",new BigDecimal(map.get("newPrice").toString()).divide(new BigDecimal("100")).toString());
            BigDecimal price = new BigDecimal(map.get("price").toString()).divide(new BigDecimal("100"));
            map.put("price",price.setScale(2, ROUND_HALF_UP).toString());
            String ownSpec = map.get("ownSpec").toString().substring(1,map.get("ownSpec").toString().length()-1);
            ownSpec = ownSpec.replace("\"","");
            map.put("ownSpec",ownSpec);
            newMapList.add(map);
        }
        PageInfo pageInfo = new PageInfo<Map<String,Object>>(newMapList);
        return pageInfo;
    }

    @Override
    public PageInfo<Map<String,Object>> queryOrderRecordTotal(String shopId,Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Map<String,Object>> orderRecordList = orderRecordMapper.queryOrderRecordTotal(shopId);
        return new PageInfo<Map<String,Object>>(CommonUtil.toCamel(orderRecordList));
    }
}
