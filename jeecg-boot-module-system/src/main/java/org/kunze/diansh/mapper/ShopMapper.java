package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;
import org.kunze.diansh.entity.modelData.MonthMenuModel;

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
    List<Map<String,String>> selectStoreLeaderboard(@Param("more") String more,@Param("choiceTime") String choiceTime);


    /**
     * 门店销售统计图
     * */
   MonthMenuModel selectStoreByShop(@Param("shopId") String shopId,@Param("year") String year);
}
