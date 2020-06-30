package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;
import org.kunze.diansh.entity.modelData.MonthMenuModel;
import org.kunze.diansh.entity.modelData.SalesModel;

import java.util.List;
import java.util.Map;

public interface ShopMapper extends BaseMapper<KzShop> {

    /**
     * 根据超市id查询超市信息
     * @param id
     * @return
     */
    KzShop selectByKey(@Param("shopId") String id);

    List<ShopVo> queryShopList(KzShop shop);


    /***
     * 添加超市信息
     * @param shop
     * @return
     */
    int insertShop(KzShop shop);


    /***
     * 修改超市信息
     * @param shop
     * @return
     */
    int updateShop(KzShop shop);

    /***
     * 超市销售排行
     * @return
     */
    List<Map<String,Object>> selectStoreLeaderboard(@Param("shopName")String shopName, @Param("more") String more,@Param("choiceTime") String choiceTime);


    /**
     * 门店销售统计图
     * */
   MonthMenuModel selectStoreByShop(@Param("shopId") String shopId,@Param("year") String year);


    /***
     * 查询总订单量和当日订单量
     * @param shopId
     * @return
     */
   Map<String,String> selectOrderLeader(@Param("shopId") String shopId);

    /***
     * 查询近10天的订单量
     * @param shopId
     * @return
     */
   List<Map<String,String>> selectOrderLeaders(@Param("shopId") String shopId);


    /**
     * 查询销售额
     * @param shopId
     * @return
     */
   SalesModel selectSales(@Param("shopId") String shopId);


    /***
     * 查询超市当月销售额
     * @param shopId
     * @return
     */
   Map<String,String> selectMonthMoney(@Param("shopId") String shopId);

    /**
     * 查询上个月的汇总信息
     * @param shopId
     * @return
     */
   Map<String,String> selectOldMonthMoney(@Param("shopId") String shopId);

    /**
     * 查询超市当天销售额
     * @param shopId
     * @return
     */
   Map<String,String> selectToDayMoney(@Param("shopId") String shopId);

    /**
     * 查询超市的商品总数(包含上架和未上架)
     * @param shopId
     * @return
     */
   String selectTotalSpuNum(@Param("shopId") String shopId);


    /**
     * 查询超市的总订单
     * @param shopId
     * @return
     */
   String selectTotalOrder(@Param("shopId") String shopId);


    /***
     * 订单统计
     * @param shopId
     * @return
     */
   Map<String,String> selectOrderstatistics(@Param("shopId") String shopId);


    /**
     * 库存统计
     * @param shopId
     * @return
     */
   Map<String,String> selectWarehouseStatistics(@Param("shopId") String shopId);


    /***
     * 查询近7天的成交量
     * @param shopId
     * @return
     */
   List<Map<String,String>> selectSevenDeal(@Param("shopId") String shopId);


    /**
     * 删除超市
     * @return
     */
   int delShops(@Param("delShop") List<String> list);

    /**
     * 查询超市id的集合
     * @return
     */
   List<String> selectShopIds();
}
