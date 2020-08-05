package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.SkuVo;
import org.kunze.diansh.entity.HotelSku;
import org.kunze.diansh.entity.Sku;

import java.util.List;
import java.util.Map;

public interface ISkuService extends IService<Sku> {

    /**
     * 根据spuId查询相关联的sku
     * @param spuId
     * @return
     */
    List<Sku> querySkuBySpuId(String spuId);

    /**
     * 查询商品展示的详细信息 通过spuId
     * @param spuId
     * @return
     */
    List<Map<String,Object>> selectSkuInfoBySpuId(String spuId);

    /***
     * 查询不是特卖商品的规格
     * @param spuId
     * @return
     */
    PageInfo<Sku> queryNotFeatSku(@Param("spuId") String spuId,Integer pageNo,Integer pageSize);

    /**
     * 添加规格
     * @param skuVo
     * @return
     */
    Boolean saveSku(SkuVo skuVo);

    /**
     * 添加sku 类型为餐饮
     * @param hotelSku
     * @return
     */
    Boolean addHotelSku(HotelSku hotelSku);

    /**
     * 修改sku 类型为餐饮
     * @param hotelSku
     * @return
     */
    Boolean updateHotelSku(HotelSku hotelSku);

    /**
     * 删除sku 通过id 类型为餐饮
     * @param id
     * @return
     */
    Boolean delHotelSkuById(String id);

    /**
     * 查询sku 通过店铺id 类型为餐饮
     * @param shopId
     * @return
     */
    PageInfo<Map<String,Object>> queryHotelSku(String shopId,Integer pageNo,Integer pageSize);

    /**
     * 查询sku 通过分类id 类型为餐饮
     * @param shopId
     * @param cid
     * @return
     */
    List<Map<String,Object>> queryHotelSkuByCid(String shopId,String cid,String saleable);

    /**
     * 检索hotelSku 类型为餐饮
     * @param shopId
     * @param title
     * @return
     */
    PageInfo<Map<String,Object>> searchHotelSku(String shopId,String title,Integer pageNo,Integer pageSize);

    /**
     * 通过id查询hotelSku 类型为餐饮
     * @param id
     * @return
     */
    HotelSku queryHotelById(String id);
}
