package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.RiderSendVo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.*;

import java.util.List;

@Mapper
public interface RiderSendMapper extends BaseMapper<RiderSend> {

    /**
     *
     *查看更多订单
     * **/
   List<RiderSend> queryHitoryOrderList(@Param(value = "riderSend")RiderSend riderSend);
   /**
    * 修改骑手状态
    *
    * **/

   int updateState(@Param(value = "riderSend")RiderSend riderSend);

   /**
    * 根据shopId查询商家信息
    *
    * **/

   ShopVo queryShopInfo(@Param(value = "shopId")String shopId);

   /**
    * 添加配送信息
    * @param riderSendVo
    * @return
    */
   int saveRiderSend(@Param(value = "riderSendVo") RiderSendVo riderSendVo);

   /**
    * 查看派送订单
    *
    * **/
   RiderSend queryRiderOrder(@Param(value = "riderSend")RiderSend riderSend);



   /**
    * 修改骑手状态
    *
    * **/

   int updateIsTurn(@Param(value = "riderSend")RiderSend riderSend);

   /**
    *
    * 查看骑手信息
    * **/
   List<Riders> queryRidersInfo(@Param(value = "riders")Riders riders);

   /**
    * 查看超市所有订单
    * **/

   List<SupOrder> queryShopOrderList(@Param(value = "area")String area);

   /**
    * 添加骑手订单派送信息
    * **/

   int saveRiderSendOrder(@Param(value = "riderSendVo") RiderOrder riderOrder);
}
