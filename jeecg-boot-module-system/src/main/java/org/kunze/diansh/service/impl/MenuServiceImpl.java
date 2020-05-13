package org.kunze.diansh.service.impl;

import org.kunze.diansh.entity.modelData.MonthMenuModel;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        stringList.add(monthMenuModel.getJanuary());
        stringList.add(monthMenuModel.getFebruary());
        stringList.add(monthMenuModel.getMarch());
        stringList.add(monthMenuModel.getApril());
        stringList.add(monthMenuModel.getMay());
        stringList.add(monthMenuModel.getJune());
        stringList.add(monthMenuModel.getJuly());
        stringList.add(monthMenuModel.getAugust());
        stringList.add(monthMenuModel.getSeptember());
        stringList.add(monthMenuModel.getOctober());
        stringList.add(monthMenuModel.getNovember());
        stringList.add(monthMenuModel.getDecember());
        return stringList;
    }
}
