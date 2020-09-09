package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.HotelSku;
import org.kunze.diansh.entity.OrderDetail;
import org.kunze.diansh.entity.Sku;

import java.util.List;
import java.util.Map;

public interface HotelSkuMapper extends BaseMapper<HotelSku> {

    //添加sku 类型为餐饮
    Integer addHotelSku(HotelSku hotelSku);

    //修改sku 类型为餐饮
    Integer updateHotelSku(@Param("hotelSku") HotelSku hotelSku);

    //删除sku 通过id 类型为餐饮
    Integer delHotelSkuById(@Param("id") String id);

    //查询sku 通过店铺id 类型为餐饮
    List<Map<String,Object>> queryHotelSku(@Param("hotelSku") HotelSku hotelSku);

    //查询商品的基本信息 通过skuid
    HotelSku queryHotelById(@Param("id") String skuId);

    /**
     * 批量获取商品信息 通过orderDetail中的skuId
     * @return
     */
    List<HotelSku> getHotelSkusByOrder(@Param("list")List<OrderDetail> list);

    //检索hotelSku
    List<Map<String,Object>> searchHotelSku(@Param("shopId") String shopId,@Param("title") String title);
}
