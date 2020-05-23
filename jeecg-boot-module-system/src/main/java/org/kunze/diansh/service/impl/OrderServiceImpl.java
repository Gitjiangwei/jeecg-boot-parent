package org.kunze.diansh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.EmptyUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Or;
import org.jeecg.common.util.OrderCodeUtils;
import org.jeecg.modules.message.mapper.SysUserShopMapper;
import org.jeecg.modules.message.websocket.WebSocket;
import org.kunze.diansh.controller.bo.OrderBo;
import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.OrderSpuVo;
import org.kunze.diansh.controller.vo.OrderVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.entity.modelData.OrderModel;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.OrderRecordMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.pust.Demo;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    CartServiceImpl cartService = new CartServiceImpl();

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private SysUserShopMapper sysUserShopMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    /**
     * 创建订单
     * @param aid 地址id
     * @param cids 购物车集合
     * @param shopId 店铺id
     * @param userID 用户id
     * @param pick_up 配送方式 1.自提 2.商家配送
     */
    @Override
    @Transactional
    public Order createOrder(String aid, List cids, String shopId, String userID,String pick_up) {
        //当前时间
        Date date = new Date();
        //根据aid查找相关的地址信息
        //Address address = addressMapper.selectAddressByID(aid);

        //根据cids获取购买的物品的信息
        List<Cart> cartList = cartService.selectCartByCids(cids,userID);

        //总价格
        Integer totalPrice = 0;

        //计算订单总价
        for (Cart c:cartList) {
            BigDecimal unitPrice = new BigDecimal(c.getCartPrice());
            BigDecimal num = new BigDecimal(c.getCartNum());
            BigDecimal price = unitPrice.multiply(num);
            totalPrice = totalPrice+price.intValue();
        }

        Integer orderNum = orderMapper.selectShopOrderNum(shopId);

        //订单
        Order order = new Order();
        KzShop shop = shopMapper.selectByKey(shopId);
        if(shop == null){
            return  null;
        }
        //生成订单号
        order.setOrderId(OrderCodeUtils.orderCode(shop.getShopName(),orderNum.toString()));
        order.setShopId(shopId); //店铺id
        order.setUserID(userID); //用户id
        order.setPickUp(pick_up); //配送方式
        order.setAddressId(EmptyUtils.isEmpty(aid)?"":aid); //地址
        order.setCreateTime(date);//创建时间
        order.setCancelTime(OrderCodeUtils.createCancelTime(date)); //取消时间 订单的生命周期
        order.setUpdateTime(date);//更新时间
        order.setAmountPayment(totalPrice.toString()); //应付金额
        order.setPayment("0"); //实付金额
        order.setStatus(1); //订单状态 未付款

        //插入订单数据
        Integer rows = orderMapper.insertOrder(order);
        if (rows != 1){
            new Exception("创建订单失败！插入订单时出现未知错误");
        }

        List<OrderDetail> odList = new ArrayList<OrderDetail>();
        for (Cart cart:cartList) {
            OrderDetail od = new OrderDetail();
            od.setId(UUID.randomUUID().toString().replace("-",""));
            od.setOrderId(order.getOrderId());
            od.setSkuId(cart.getSkuid());
            od.setNum(cart.getCartNum());
            od.setTitle(cart.getTitile());
            od.setOwnSpec(cart.getOwnSpec());
            od.setPrice(Integer.parseInt(cart.getCartPrice()));
            od.setImage(cart.getImage());
            Integer odRows = orderMapper.insertOrderDetail(od);
            odList.add(od);
            if (rows != 1){
                new Exception("创建订单失败！插入订单时出现未知错误");
            }
        }
        return order;
    }

    /**
     * 根据订单状态查询订单数据
     * @param status 订单状态
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    @Override
    public List<Order> selectOrderByStatus(String status, String userID, String shopID) {
        List<Order>  orders = orderMapper.selectOrderByStatus(status,userID,shopID);
        if(orders.size() != 0){
            for (int  i=0;i<orders.size();i++) {
                Order order = orders.get(i);
                List<OrderDetail> odlist = orderMapper.selectOrderDetailById(order.getOrderId());
                order.setOdList(odlist);
                orders.set(i,order);
            }
        }
        return orders;
    }

    /**
     * 根据订单ID查询订单数据
     * @param orderId 订单id
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    @Override
    public Order selectOrderById(String orderId, String userID, String shopID) {
        Order order = orderMapper.selectOrderById(orderId,userID,shopID);
        //取消时间
        order.setCancelTime(OrderCodeUtils.createCancelTime(order.getCreateTime()));

        //商品详细信息集合
        List<OrderDetail> odlist = orderMapper.selectOrderDetailById(order.getOrderId());
        order.setOdList(odlist);
        order.setAmountPayment(this.countOrderPayment(odlist));
        return order;
    }

    /***
     * 订单支付后修改状态
     * @param orderId
     * @return
     */
    @Override
    public String updateOrderStatus(String orderId) {
        String flag = "error";
        //1、修改订单状态为【已支付】
        int orderStatus = orderMapper.updateOrderStatus("2",orderId);
        orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"用户下单"));
        if(orderStatus > 0){
            //获取订单信息
            Order order = orderMapper.selectById(orderId);
            if(order==null){
                return flag;
            }
            List<String> stringList = sysUserShopMapper.selectByIds(order.getShopId());

            JSONObject obj = new JSONObject();
            if(stringList.size()>0 && stringList.size() == 1){
                //通过WebScoket进行发送
                obj.put("cmd","user");
                obj.put("userId",stringList.get(0));
                obj.put("msgId", "M0001");
                obj.put("msgTxt", "您有一条新的订单，请注意查收");
                webSocket.sendOneMessage(stringList.get(0),obj.toJSONString());
                //通过友盟进行消息推送
                Demo demo = new Demo("5eb2184adbc2ec0856ab2aac","xe2gzni0gkjesy8mfomucngiddpiumm1");
                try {
                    demo.sendAndroidBroadcast();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {

            }
            flag = "OK";
        }
        return flag;
    }

    /**
     * 订单支付后修改状态
     * @param status
     * @param orderId
     * @return
     */
    @Override
    public String updateOrderStatus(String status,String orderId) {
        String flag = "error";
        int isSuccess = orderMapper.updateOrderStatus(status,orderId);
        if(status.equals("3")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"商家接单"));
        }else if(status.equals("4")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"配货完成，开始配送"));
        }else if(status.equals("5")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"用户收到商品，订单完成"));
        }else {
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"异常订单，订单关闭"));
        }
        if(isSuccess>0){
            flag = "ok";
        }
        return flag;
    }


    /**
     * 计算商品总价格
     * @param odList
     * @return
     */
    private String countOrderPayment(List<OrderDetail> odList){
        Integer totalPrice = 0;
        //计算订单总价
        for (OrderDetail od:odList) {
            BigDecimal unitPrice = new BigDecimal(od.getPrice());
            BigDecimal num = new BigDecimal(od.getNum());
            BigDecimal price = unitPrice.multiply(num);
            totalPrice = totalPrice+price.intValue();
        }
        return totalPrice.toString();
    }

    /***
     * 后台管理系统查询订单数据
     * @param shopId
     * @param status
     * @param orderId
     * @return
     */
    @Override
    public PageInfo<OrderVo> selectOrder(String shopId, String status, String orderId,Integer pageNo,Integer pageSize) {
        if(shopId == null || shopId.equals("")){
            return null;
        }else {
            Order order = new Order();
            order.setShopId(shopId);
            if(status!=null && !status.equals("")) {
                order.setStatus(Integer.valueOf(status));
            }
            if(orderId!=null && !orderId.equals("")) {
                order.setOrderId(orderId);
            }
            Page page= PageHelper.startPage(pageNo,pageSize);
            List<OrderModel> orderModels = orderMapper.selectOrder(order);
            List<OrderVo> orderVos = new ArrayList<OrderVo>();
            for (int i = 0;i<orderModels.size(); i++){
                OrderVo orderVo = new OrderVo();
                BeanUtils.copyProperties(orderModels.get(i),orderVo);
                String sex = "";
                if(orderModels.get(i).getConsigneeSex() != null && !orderModels.get(i).getConsigneeSex().equals("")) {
                    if (orderModels.get(i).getConsigneeSex().equals("2")) {
                        sex = "女士";
                    } else {
                        sex = "先生";
                    }
                }
                orderVo.setConsigneeSex(orderModels.get(i).getConsignee()==null?"":orderModels.get(i).getConsignee()+sex);
                orderVos.add(orderVo);
            }
            PageInfo pageInfo = new PageInfo<OrderVo>(orderVos);
            pageInfo.setTotal(page.getTotal());
            return pageInfo;
        }
    }

    /***
     * 查询订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo selectOrderDetail(String orderId) {
        if(!EmptyUtils.isEmpty(orderId)){
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            //1、根据订单ID查询到订单信息
            Order order = orderMapper.selectById(orderId);
            //2、根据订单信息中pickUp来判断是自提还是商家配送
            //3、根据订单信息中的地址id获取用户地址查询配送地址
            Address address = addressMapper.selectAddressByID(order.getAddressId());
            //4、根据订单id来获取详细订单中商品信息
            List<OrderDetail> orderDetails = orderMapper.selectOrderDetailById(orderId);
            //6、添加配送信息
            DistributionVo distributionVo = new DistributionVo();
            String sex = "";
            if(address.getConsigneeSex()== 0){
                sex = "先生";
            }else if(address.getConsigneeSex() == 1){
                sex = "女士";
            }
            String call = address.getConsignee()+sex;
            distributionVo.setCall(call);
            String tel = address.getTelphone()==null?"":address.getTelphone();
            distributionVo.setContact(tel);
            if(order.getPickUp().equals("2")){
                //6.1、根据pickUp来判断是商家配送还是自提
                String addres = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet();
                distributionVo.setShippingAddress(addres);
            }else {
                distributionVo.setShippingAddress("自提");
            }
            orderDetailVo.setDistributionVo(distributionVo);
            List<OrderSpuVo> orderSpuVos = new ArrayList<>();
            BigDecimal totalNum = new BigDecimal("0"); //商品总数
            for (int i =0;i<orderDetails.size();i++){
                //商品名称
                String title = orderDetails.get(i).getTitle();
                //商品单价
                String price = orderDetails.get(i).getPrice().toString();
                BigDecimal prices = new BigDecimal(price);
                price = prices.multiply(new BigDecimal("0.01")).toString();
                //商品数量
                String num = orderDetails.get(i).getNum().toString();
                //商品金额
                BigDecimal priceOne = new BigDecimal(price);
                BigDecimal spuNum = new BigDecimal(num);
                BigDecimal totalPrice = priceOne.multiply(spuNum);
                totalNum = totalNum.add(spuNum);
                OrderSpuVo orderSpuVo = new OrderSpuVo();
                orderSpuVo.setImage(orderDetails.get(i).getImage());
                orderSpuVo.setSpuName(title);
                String ownSpec = orderDetails.get(i).getOwnSpec()==null?"":orderDetails.get(i).getOwnSpec();
                if(!ownSpec.equals("")){
                    ownSpec = ownSpec.substring(1,ownSpec.length()-1);
                }
                orderSpuVo.setOwenSpan(ownSpec);
                orderSpuVo.setSpuNum(num);
                orderSpuVo.setUnitPrice(price);
                orderSpuVo.setUnitPriceTotle(totalPrice.toString());
                orderSpuVos.add(orderSpuVo);
            }
            orderDetailVo.setOrderSpuVos(orderSpuVos);
            //8、填充订单其它信息
            orderDetailVo.setOrderId(orderId);//商品ID
            orderDetailVo.setBuyerMessage(order.getBuyerMessage()==null?"买家没有留言呦！":order.getBuyerMessage()); //买家留言
            orderDetailVo.setCreateTime(order.getPaymentTime()); //下单时间
            orderDetailVo.setPostFree(order.getPostFee()==null?"0":order.getPostFee());//配送费
            orderDetailVo.setSaleNum(totalNum.toString());//商品总数
            BigDecimal amout = new BigDecimal(order.getAmountPayment());
            amout = amout.multiply(new BigDecimal("0.01"));
            orderDetailVo.setSaleSum(amout.toString()); //商品总金额
            BigDecimal payAmout = new BigDecimal(order.getPayment());
            payAmout = payAmout.multiply(new BigDecimal("0.01"));
            orderDetailVo.setPractical(payAmout.toString());//实付金额
            return orderDetailVo;
        }else {
            return null;
        }
    }

    /***
     * 查询订单记录
     * @param orderId
     * @return
     */
    @Override
    public List<Map<String, String>> queryOrderRecord(String orderId) {
        if(orderId==null || orderId.equals("")){
            return null;
        }else {
            List<Map<String,String>> mapList = orderRecordMapper.queryOrderRecord(orderId);
            return mapList;
        }
    }


}
