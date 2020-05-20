package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.KzShop;

public interface IShopService extends IService<KzShop> {

    /***
     * 查询所有商家
     * @param shopVo
     * @return
     */
    PageInfo<ShopVo> queryShopList(ShopVo shopVo,Integer pageNo,Integer pageSize);


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
}
