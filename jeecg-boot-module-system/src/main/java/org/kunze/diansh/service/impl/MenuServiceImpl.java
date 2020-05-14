package org.kunze.diansh.service.impl;

import org.kunze.diansh.controller.vo.SalesVo;
import org.kunze.diansh.entity.modelData.MonthMenuModel;
import org.kunze.diansh.entity.modelData.SalesModel;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 门店销售排行榜
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectStoreLeaderboard(String more,String choiceTime) {
        List<Map<String,String>> newMapList = new ArrayList<Map<String, String>>();
        List<Map<String,String>> oldMapList = shopMapper.selectStoreLeaderboard(more,choiceTime);
        for(int i =0;i< oldMapList.size();i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("shopId",oldMapList.get(i).get("id") == null?"":oldMapList.get(i).get("id"));
            map.put("shopName",oldMapList.get(i).get("shopName") == null?"":oldMapList.get(i).get("shopName"));
            map.put("payment",oldMapList.get(i).get("payment") == null?"0":String.valueOf(oldMapList.get(i).get("payment")));
            newMapList.add(map);
        }
        return newMapList;
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
            List<Map<String,String>> mapList = shopMapper.selectStoreLeaderboard("0","0");
            shopId = mapList.get(0).get("id") == null?"":mapList.get(0).get("id");
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
            stringList.add(monthMenuModel.getJanuary()==null?"0":monthMenuModel.getJanuary());
            stringList.add(monthMenuModel.getFebruary()==null?"0":monthMenuModel.getFebruary());
            stringList.add(monthMenuModel.getMarch()==null?"0":monthMenuModel.getMarch());
            stringList.add(monthMenuModel.getApril()==null?"0":monthMenuModel.getApril());
            stringList.add(monthMenuModel.getMay()==null?"0":monthMenuModel.getMay());
            stringList.add(monthMenuModel.getJune()==null?"0":monthMenuModel.getJune());
            stringList.add(monthMenuModel.getJuly()==null?"0":monthMenuModel.getJuly());
            stringList.add(monthMenuModel.getAugust()==null?"0":monthMenuModel.getAugust());
            stringList.add(monthMenuModel.getSeptember()==null?"0":monthMenuModel.getSeptember());
            stringList.add(monthMenuModel.getOctober()==null?"0":monthMenuModel.getOctober());
            stringList.add(monthMenuModel.getNovember()==null?"0":monthMenuModel.getNovember());
            stringList.add(monthMenuModel.getDecember()==null?"0":monthMenuModel.getDecember());
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
        salesVo.setTotal(salesModel.getTotal());
        //4、当天销售额
        salesVo.setToDays(salesModel.getToday());
        return salesVo;
    }
}
