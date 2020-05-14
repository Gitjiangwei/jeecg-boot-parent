package org.kunze.diansh.service;

import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SalesVo;

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


    /***
     * 查询总订单量和当前完成的订单量
     * @param shopId
     * @return
     */
    Map<String,String> selectOrderLeader(String shopId);


    /***
     * 查询近10天的订单量
     * @param shopId
     * @return
     */
    List<Map<String,String>> selectOrderLeaders(String shopId);

    /**
     * 查询销售额
     * @param shopId
     * @return
     */
    SalesVo selectSales(String shopId);
}
