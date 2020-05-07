package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.Shop;

import java.util.List;

public interface ShopMapper extends BaseMapper<Shop> {

    /**
     * 根据超市id查询超市信息
     * @param id
     * @return
     */
    Shop selectByKey(@Param("shopId") String id);

    List<ShopVo> queryShopList(Shop shop);


    /***
     * 添加超市信息
     * @param shop
     * @return
     */
    int insertShop(Shop shop);


    /***
     * 修改超市信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
