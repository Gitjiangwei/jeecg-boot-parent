package org.kunze.diansh.service;

import java.util.List;
import java.util.Map;

public interface IMenuService {

    /**
     * 门店销售排行榜
     * @return
     */
    List<Map<String,String>> selectStoreLeaderboard(String more,String choiceTime);


    /***
     * 门店销售统计图
     * @param shopId
     * @param year
     * @return
     */
    List<String> selectStoreByShop(String shopId,String year);

}
