package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IShopService extends IService<KzShop> {

    /***
     * 查询所有商家
     * @param shopVo
     * @return
     */
    PageInfo<ShopVo> queryShopList(ShopVo shopVo,Integer pageNo,Integer pageSize);



    List<ShopVo> queryShopLists();

    /***
     * 添加超市信息
     * @param shop
     * @return
     */
    Boolean insertShop(KzShop shop);


    /**
     * 修改超市信息
     * @param shop
     * @return
     */
    Boolean updateShop(KzShop shop);

    /***
     * 删除超市信息
     * @param shopId
     * @return
     */
    Boolean delShops(String shopId);

    /**
     * 通过超市id查询超市信息
     * @param shopId
     * @return
     */
    List<Map<String,Object>> selectShopInfoById(String shopId);


    /**
     * 修改商家配送方式
     * @param shopId
     * @param distModel
     * @param postFree
     * @return
     */
    Boolean editShopDistModel(String shopId,String distModel,String postFree);
}
