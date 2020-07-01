package org.kunze.diansh.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.OrderVo;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.OrderDetail;

import java.util.List;
import java.util.Map;


public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车集合
     * @param shopId 店铺id
     * @param userID 用户id
     * @param pick_up 配送方式 1.自提 2.商家配送
     * @param postFree 配送费
     * @param buyerMessage 备注
     */
    Order createOrder(String aid, JSONArray cids, String shopId, String userID, String pick_up,String postFree,Integer payType,String buyerMessage);

    /**
     * 根据订单状态查询订单数据
     * @param status 订单状态
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    List<Order> selectOrderByStatus(String status, String userID, String shopID);


    /**
     * 根据订单ID查询订单数据
     * @param orderId 订单id
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    Order selectOrderById(String orderId,String userID,String shopID);

    /***
     * 订单支付后修改状态
     * @param orderId
     * @return
     */
    String updateOrderStatus(String orderId,String payment);


    /**
     * 订单支付后修改状态
     * @param status 状态
     * @param orderId 订单id
     * @return
     */
    String updateOrderStatu(String status,String orderId);



    /***
     * 后台管理系统查询订单数据
     * @param shopId
     * @param status
     * @param orderId
     * @return
     */
    PageInfo<OrderVo> selectOrder(String shopId, String status,String telphone, String orderId,Integer pageNo,Integer pageSize);


    /***
     * 查询订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo selectOrderDetail(String orderId);

    /***
     * 查询订单记录
     * @param orderId
     * @return
     */
    List<Map<String,String>> queryOrderRecord(String orderId);

    /***
     * 查询订单数据
     * @param orderId
     * @return
     */
    Order selectById(String orderId);

    /**
     * 计算商品总价格
     * @param odList 订单详细数据的集合
     * @return 价格
     */
    String countOrderPayment(List<OrderDetail> odList);

    /**
     * 根据用户id查询当前是否有未支付的订单
     * @param userId
     * @return
     */
    Boolean selectOrderByUserId(String userId,String shopId);

    /**
     * 根据订单ID查询订单数据
     * @param orderId 订单id
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    Map<String,Object> againOrder(String orderId, String userID, String shopID);
}
