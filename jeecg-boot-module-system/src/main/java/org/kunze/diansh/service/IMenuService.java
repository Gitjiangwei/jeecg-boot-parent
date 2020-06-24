package org.kunze.diansh.service;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.InformationVo;
import org.kunze.diansh.controller.vo.SalesVo;
import org.kunze.diansh.entity.OrderRecord;

import java.util.List;
import java.util.Map;

public interface IMenuService {

    /**
     * 门店销售排行榜
     * @return
     */
    PageInfo<Map<String, String>> selectStoreLeaderboard(String shopName, String more,String choiceTime,Integer pageNo,Integer pageSize);


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

    /**
     * 查询平台数据统计
     * @param shopId
     * @return
     */
    InformationVo selectInfo(String shopId);

    /***
     * 订单统计
     * @param shopId
     * @return
     */
    Map<String,String> selectOrderstatistics(String shopId);


    /***
     * 库存统计
     * @param shopId
     * @return
     */
    Map<String,String> selectWarehouseStatistics(String shopId);


    /**
     * 查询近7天的成交量
     * @param shopId
     * @return
     */
    List<Map<String,String>> selectSevenDeal(String shopId);


    /**
     * 查询库存
     * @param shopId
     * @return
     */
    PageInfo<Map<String,Object>> selectStock(String shopId,String title,String enable,Integer pageNo,Integer pageSize);


    PageInfo<Map<String,Object>> queryOrderRecordTotal(String shopId,Integer pageNo,Integer pageSize);
}
