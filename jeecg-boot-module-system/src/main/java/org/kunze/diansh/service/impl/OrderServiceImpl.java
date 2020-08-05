package org.kunze.diansh.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.EmptyUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jeecg.common.util.OrderCodeUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.message.mapper.SysUserShopMapper;
import org.jeecg.modules.message.websocket.WebSocket;
import org.kunze.diansh.controller.bo.OrderParams;
import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.OrderDetailVo;
import org.kunze.diansh.controller.vo.OrderSpuVo;
import org.kunze.diansh.controller.vo.OrderVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.entity.modelData.OrderModel;
import org.kunze.diansh.mapper.*;
import org.kunze.diansh.pust.Demo;
import org.kunze.diansh.service.IOrderService;
import org.kunze.diansh.service.IStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private SysUserShopMapper sysUserShopMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HotelSkuMapper hotelSkuMapper;

    /**
     * 创建订单
     * aid 地址id
     * cids 购物车集合
     * shopId 店铺id
     * userID 用户id
     * pick_up 配送方式 1.自提 2.商家配送
     * postFree 配送费
     * buyerMessage 备注
     */
    @Transactional
    @Override
    public Result createOrder(OrderParams params) {
        Result result = new Result();
        String shopId = params.getShopId();
        String userId = params.getUserID();
        String pickUp = params.getPick_up();
        String postFree = params.getPostFree();
        String buyerMessage = params.getBuyerMessage();
        String aid = params.getAid();
        Integer payType = params.getPayType();
        Long buyerPhone = params.getBuyerPhone();
        String buyerNick = params.getBuyerNick();

        //当前时间
        Date date = new Date();
        //根据aid查找相关的地址信息
        Address address = addressMapper.selectAddressByID(aid);

        //判斷書否是超市還是飯店


        //根据cids获取购买的物品的信息
        List<Sku> cartList = this.selectSkuList(params.getCids());

        Boolean stockFlag = this.costStockInfo(cartList);
        if(stockFlag){
            return result.error500("库存不足！");
        }
        //总价格
        Integer totalPrice = this.getTotalPrice(cartList);

        //店铺当天的订单数
        Integer orderNum = orderMapper.selectShopOrderNum(shopId);

        //订单
        Order order = new Order();
        KzShop shop = shopMapper.selectByKey(shopId);
        if(shop == null){
            return result.error500("店铺为空！");
        }
        if(totalPrice < shop.getMinPrice()){
            return result.error500("订单价格不足最低起送价！");
        }
        //生成订单号
        order.setOrderId(OrderCodeUtils.orderCode(shop.getShopName(),orderNum.toString()));
        order.setShopId(shopId); //店铺id
        order.setUserID(userId); //用户id
        order.setPickUp(pickUp); //配送方式
        order.setAddressId(EmptyUtils.isEmpty(aid)?"":aid); //地址
        order.setCreateTime(date);//创建时间
        order.setCancelTime(OrderCodeUtils.createCancelTime(date)); //取消时间 订单的生命周期
        order.setUpdateTime(date);//更新时间
        order.setAmountPayment(totalPrice.toString()); //应付金额
        if(Integer.parseInt(pickUp) == 1){
            //自提不计配送费
            order.setPostFree("0");
            order.setBuyerPhone(buyerPhone);
            order.setBuyerNick(buyerNick);
        }else {
            order.setPostFree(postFree);//配送费
        }
        order.setPayment("0"); //实付金额
        order.setStatus(1); //订单状态 未付款
        order.setPayType(payType); //付款类型
        order.setBuyerMessage(buyerMessage);//备注
        order.setPickNo(RandomUtil.randomNumbers(4)); //取货码

        //插入订单数据
        Integer rows = orderMapper.insertOrder(order);
        if (rows != 1){
            new Exception("创建订单失败！插入订单时出现未知错误");
            return result.error500("创建订单失败！插入订单时出现未知错误!");
        }

        List<OrderDetail> odList = new ArrayList<OrderDetail>();
        for (Sku sku:cartList) {
            OrderDetail od = new OrderDetail();
            od.setId(UUID.randomUUID().toString().replace("-",""));
            od.setOrderId(order.getOrderId());
            od.setSkuId(sku.getId());
            od.setNum(sku.getNum());
            od.setTitle(sku.getTitle());
            od.setOwnSpec(sku.getOwnSpec());
            if(sku.getIsFeatures().equals("1")){
                SpuFeatures feat = spuFeaturesMapper.selectFeatBySkuId(sku.getId());
                sku.setPrice(feat.getFeaturesPrice());
            }else if (sku.getNewPrice()!= null && !sku.getNewPrice().equals("") && !sku.getNewPrice().equals("0")){
                sku.setPrice(sku.getNewPrice());
            }
            od.setPrice(Integer.parseInt(sku.getPrice()));
            od.setImage(sku.getImages());
            Integer odRows = orderMapper.insertOrderDetail(od);
            odList.add(od);
            if (odRows != 1){
                new Exception("创建订单失败！插入订单时出现未知错误");
                return result.error500("创建订单失败！插入订单时出现未知错误!");
            }
        }
        result.setResult(order);
        return result;
    }

    /**
     * 创建订单
     * aid 地址id
     * cids 购物车集合
     * shopId 店铺id
     * userID 用户id
     * pick_up 配送方式 1.自提 2.商家配送
     * postFree 配送费
     * buyerMessage 备注
     */
    @Transactional
    @Override
    public Result createHotelOrder(OrderParams params) {
        Result result = new Result();
        String shopId = params.getShopId();
        String userId = params.getUserID();
        String pickUp = params.getPick_up();
        String postFree = params.getPostFree();
        String buyerMessage = params.getBuyerMessage();
        String aid = params.getAid();
        Integer payType = params.getPayType();
        Long buyerPhone = params.getBuyerPhone();
        String buyerNick = params.getBuyerNick();

        //当前时间
        Date date = new Date();
        //根据aid查找相关的地址信息
        Address address = addressMapper.selectAddressByID(aid);

        //判斷書否是超市還是飯店


        //根据cids获取购买的物品的信息
        List<HotelSku> cartList = this.selectHotelSkuList(params.getCids());

        //总价格todo
        Integer totalPrice = this.getHotelTotalPrice(cartList);

        //店铺当天的订单数
        Integer orderNum = orderMapper.selectShopOrderNum(shopId);

        //订单
        Order order = new Order();
        KzShop shop = shopMapper.selectByKey(shopId);
        if(shop == null){
            return result.error500("店铺为空！");
        }
        //生成订单号
        order.setOrderId(OrderCodeUtils.orderCode(shop.getShopName(),orderNum.toString()));
        order.setShopId(shopId); //店铺id
        order.setUserID(userId); //用户id
        order.setPickUp(pickUp); //配送方式
        order.setAddressId(EmptyUtils.isEmpty(aid)?"":aid); //地址
        order.setCreateTime(date);//创建时间
        order.setCancelTime(OrderCodeUtils.createCancelTime(date)); //取消时间 订单的生命周期
        order.setUpdateTime(date);//更新时间
        order.setAmountPayment(totalPrice.toString()); //应付金额
        if(Integer.parseInt(pickUp) == 1){
            //自提不计配送费
            order.setPostFree("0");
            order.setBuyerPhone(buyerPhone);
            order.setBuyerNick(buyerNick);
        }else {
            order.setPostFree(postFree);//配送费
        }
        order.setPayment("0"); //实付金额
        order.setStatus(1); //订单状态 未付款
        order.setPayType(payType); //付款类型
        order.setBuyerMessage(buyerMessage);//备注
        order.setPickNo(RandomUtil.randomNumbers(4)); //取货码

        //插入订单数据
        Integer rows = orderMapper.insertOrder(order);
        if (rows != 1){
            new Exception("创建订单失败！插入订单时出现未知错误");
            return result.error500("创建订单失败！插入订单时出现未知错误!");
        }

        List<OrderDetail> odList = new ArrayList<OrderDetail>();
        for (HotelSku sku:cartList) {
            OrderDetail od = new OrderDetail();
            od.setId(UUID.randomUUID().toString().replace("-",""));
            od.setOrderId(order.getOrderId());
            od.setSkuId(sku.getId().toString());
            od.setNum(sku.getNum());
            od.setTitle(sku.getTitle());
            od.setOwnSpec(sku.getOwnSpec());
            if (sku.getNewPrice()!= null && !sku.getNewPrice().toString().equals("0")){
                sku.setPrice(sku.getNewPrice());
            }
            od.setPrice(sku.getPrice());
            od.setImage(sku.getImages());
            Integer odRows = orderMapper.insertOrderDetail(od);
            odList.add(od);
            if (odRows != 1){
                new Exception("创建订单失败！插入订单时出现未知错误");
                return result.error500("创建订单失败！插入订单时出现未知错误!");
            }
        }
        result.setResult(order);
        return result;
    }


    /**
     * 预占库存
     * @param skuList
     * @return
     */
    public Boolean costStockInfo(List<Sku> skuList){
        Boolean flag = false;
        for(Sku sku:skuList){
            if(stockService.contrastStockBySkuId(sku.getId(),sku.getNum().toString())){
                flag = true;
                return flag;
            }
        }
        //预占库存
        for (Sku s:skuList) {
            Double decrValue= redisUtil.hdecr(IStockService.StockType.STOCK_PREFIX,s.getId(),s.getNum());
            if(decrValue < 0){
                flag = true;
                redisUtil.hincr(IStockService.StockType.STOCK_PREFIX,s.getId(),s.getNum());
            }
        }
        return flag;
    }


    /**
     * 计算订单总价
     * @param cartList
     * @return
     */
    private Integer getTotalPrice(List<Sku> cartList){
        Integer totalPrice = Integer.parseInt("0");
        for (Sku s:cartList) {
            if(s.getIsFeatures().equals("1")){
                //如果是特卖则获取特卖价格
                SpuFeatures feat = spuFeaturesMapper.selectFeatBySkuId(s.getId());
                s.setPrice(feat.getFeaturesPrice());
            }else if (s.getNewPrice()!= null && !s.getNewPrice().equals("") && !s.getNewPrice().equals("0")){
                //最新价格
                s.setPrice(s.getNewPrice());
            }
            BigDecimal price = NumberUtil.mul(s.getPrice(),s.getNum().toString());
            totalPrice = totalPrice+price.intValue();
        }
        return totalPrice;
    }

    /**
     * 查询选中商品的详细数据
     * @return
     */
    private List<Sku> selectSkuList(List<Map<String,Object>> cids){
        List<Sku> skuList = new ArrayList<>();
        for (Map map:cids) {
            Sku sku = skuMapper.querySkuInfoById(map.get("skuId").toString());
            sku.setNum(Integer.parseInt(map.get("num").toString()));
            skuList.add(sku);
        }
        return skuList;
    }


    /**
     * 查询选中飯店商品的详细数据
     * @return
     */
    private List<HotelSku> selectHotelSkuList(List<Map<String,Object>> cids){
        List<HotelSku> skuList = new ArrayList<>();
        for (Map map:cids) {
            HotelSku sku = hotelSkuMapper.queryHotelById(map.get("skuId").toString());
            sku.setNum(Integer.parseInt(map.get("num").toString()));
            skuList.add(sku);
        }
        return skuList;
    }

    /**
     * 计算飯店订单总价
     * @param cartList
     * @return
     */
    private Integer getHotelTotalPrice(List<HotelSku> cartList){
        Integer totalPrice = Integer.parseInt("0");
        for (HotelSku s:cartList) {
            if (s.getNewPrice()!= null && !s.getNewPrice().toString().equals("0")){
                //最新价格
                s.setPrice(s.getNewPrice());
            }
            BigDecimal price = NumberUtil.mul(s.getPrice().toString(),s.getNum().toString());
            totalPrice = totalPrice+price.intValue();
        }
        return totalPrice;
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
        return order;
    }

    /***
     * 订单支付后修改状态
     * @param orderId
     * @return
     */
    @Override
    public String updateOrderStatus(String orderId,String payment) {
        String flag = "error";
        //1、修改订单状态为【已支付】
        int orderStatus = orderMapper.updateOrderStatus("2",orderId,payment);
        orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"用户下单","1",selectShopId(orderId)));
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
    public String updateOrderStatu(String status,String orderId) {
        String flag = "error";
        int isSuccess = orderMapper.updateOrderStatus(status,orderId,"");
        String shopId = selectShopId(orderId);
        if(status.equals("3")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"商家接单","1",shopId));
        }else if(status.equals("4")){
            Order order = orderMapper.selectById(orderId);
            if ("1".equals(order.getPickUp())){
                orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"已经通知用户取货","1",shopId));
            }else {
                orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"配货完成，开始配送","1",shopId));
            }
        }else if(status.equals("5")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"用户收到商品，订单完成","1",shopId));
        }else if(status.equals("7")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"订单已退款！","1",shopId));
        }else if(status.equals("8")){
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"拒绝接单！","1",shopId));
            //回滚redis库存
            List<OrderDetail> odList= orderMapper.selectOrderDetailById(orderId);
            stockService.rollBackStock(odList);
        }else {
            orderRecordMapper.addOrderRecord(new OrderRecord(UUID.randomUUID().toString().replace("-",""),orderId,"异常订单，订单关闭","1",shopId));
            //回滚redis库存
            List<OrderDetail> odList= orderMapper.selectOrderDetailById(orderId);
            stockService.rollBackRedisStock(odList);

        }
        if(isSuccess>0){
            flag = "ok";
        }
        return flag;
    }


    /**
     * 根据订单Id查询超市ID
     * @param orderId
     * @return
     */
    private String selectShopId(String orderId){
        Order order = orderMapper.selectById(orderId);
        return order.getShopId();
    }


    /**
     * 计算商品总价格
     * @param odList 订单详细数据的集合
     * @return 价格
     */
    @Override
    public String countOrderPayment(List<OrderDetail> odList){
        BigDecimal totalPrice = new BigDecimal(0);
        //计算订单总价
        for (OrderDetail od:odList) {
            BigDecimal price = NumberUtil.mul(od.getPrice(),od.getNum());
            totalPrice = NumberUtil.add(totalPrice,price);
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
    public PageInfo<OrderVo> selectOrder(String shopId, String status,String telphone, String orderId,Integer pageNo,Integer pageSize) {
            Order order = new Order();
            order.setShopId(shopId);
            if(status!=null && !status.equals("")) {
                order.setStatus(Integer.valueOf(status));
            }
            if(orderId!=null && !orderId.equals("")) {
                order.setOrderId(orderId);
            }
            if(telphone!=null && !telphone.equals("")){
                order.setAddressId(telphone);
            }
            Page page= PageHelper.startPage(pageNo,pageSize);
            List<OrderModel> orderModels = orderMapper.selectOrder(order);
            List<OrderVo> orderVos = new ArrayList<OrderVo>();
            for (int i = 0;i<orderModels.size(); i++){
                OrderVo orderVo = new OrderVo();
                BeanUtils.copyProperties(orderModels.get(i),orderVo);
                String sex = "";
                if(orderModels.get(i).getConsigneeSex() != null && !orderModels.get(i).getConsigneeSex().equals("")) {
                    if (orderModels.get(i).getConsigneeSex().equals("0")) {
                        sex = "女士";
                    } else {
                        sex = "先生";
                    }
                }
                orderVo.setConsigneeSex(orderModels.get(i).getConsignee()==null?"":orderModels.get(i).getConsignee()+sex);
                orderVo.setPayment(new BigDecimal(orderModels.get(i).getPayment()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).toString());
                orderVos.add(orderVo);
            }
            PageInfo pageInfo = new PageInfo<OrderVo>(orderVos);
            pageInfo.setTotal(page.getTotal());
            return pageInfo;

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
                orderSpuVo.setOwenSpan(ownSpec.replace("\"",""));
                orderSpuVo.setSpuNum(num);
                orderSpuVo.setUnitPrice(price);
                orderSpuVo.setUnitPriceTotle(totalPrice.toString());
                orderSpuVos.add(orderSpuVo);
            }
            orderDetailVo.setOrderSpuVos(orderSpuVos);
            //8、填充订单其它信息
            orderDetailVo.setOrderId(orderId);//商品ID
            String remakes = "买家没有留言呦！";
            if (order.getBuyerMessage()!=null && !order.getBuyerMessage().equals("")){
                remakes = order.getBuyerMessage();
            }
            orderDetailVo.setBuyerMessage(remakes); //买家留言
            orderDetailVo.setCreateTime(order.getPaymentTime()); //下单时间
            orderDetailVo.setPostFree(order.getPostFree()==null?"0":order.getPostFree());//配送费
            orderDetailVo.setSaleNum(totalNum.toString());//商品总数
            BigDecimal amout = new BigDecimal(order.getAmountPayment());
            amout = amout.multiply(new BigDecimal("0.01"));
            orderDetailVo.setSaleSum(amout.toString()); //商品总金额
            BigDecimal payAmout = new BigDecimal(order.getPayment());
            payAmout = payAmout.multiply(new BigDecimal("0.01"));
            orderDetailVo.setPractical(payAmout.toString());//实付金额
            orderDetailVo.setPostFree(CalculationUtil.FractionalConversion(order.getPostFree()));//配送费
            //orderDetailVo.setPriceTotle(new BigDecimal(orderDetailVo.getPostFree()).add(new BigDecimal(orderDetailVo.getPractical())).toString());
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

    /***
     * 查询订单数据
     * @param orderId
     * @return
     */
    @Override
    public Order selectById(String orderId){
        Order order = new Order();
        order.setOrderId(orderId);
        return orderMapper.selectById(order);
    }

    /**
     * 根据用户id查询当前是否有未支付的订单
     * @param userId
     * @return
     */
    @Override
    public Boolean selectOrderByUserId(String userId,String shopId) {
        Integer num = orderMapper.selectOrderByUserId(userId,shopId);
        if(num >= 1){
            return true;
        }
        return false;
    }

    /**
     * 再来一单
     * @param orderId 订单id
     * @param userID 用户id
     * @param shopID 店铺id
     * @return 订单数据
     */
    @Override
    public Map<String,Object> againOrder(String orderId, String userID, String shopID) {
        Map<String,Object> map = new HashMap<>();
        Order order = orderMapper.selectOrderById(orderId,userID,shopID);
        //商品详细信息集合
        List<OrderDetail> odlist = orderMapper.selectOrderDetailById(order.getOrderId());

        List<Sku> skuList = skuMapper.getSkusByOrder(odlist);

        List<Sku> result = new ArrayList<>();
        for (OrderDetail od:odlist) {
            for (Sku sku:skuList) {
                if(od.getSkuId().equals(sku.getId())){
                    if(sku.getIsFeatures().equals("1")){
                        SpuFeatures feat = spuFeaturesMapper.selectFeatBySkuId(sku.getId());
                        sku.setPrice(feat.getFeaturesPrice());
                    }else if (sku.getNewPrice()!= null && !sku.getNewPrice().equals("") && !sku.getNewPrice().equals("0")){
                        sku.setPrice(sku.getNewPrice());
                    }
                    sku.setNum(od.getNum());
                    result.add(sku);
                }
            }
        }
        //根据aid查找相关的地址信息
        Address address = addressMapper.selectAddressByID(order.getAddressId());
        map.put("skuList",result);
        map.put("address",address);
        return map;
    }

    /**
     * 再来一单 类型为餐饮
     * @param orderId
     * @param userID
     * @param shopID
     * @return
     */
    @Override
    public Map<String, Object> againHotelOrder(String orderId, String userID, String shopID) {
        Map<String,Object> map = new HashMap<>();
        Order order = orderMapper.selectOrderById(orderId,userID,shopID);
        //商品详细信息集合
        List<OrderDetail> odlist = orderMapper.selectOrderDetailById(order.getOrderId());

        List<HotelSku> skuList = hotelSkuMapper.getHotelSkusByOrder(odlist);

        List<HotelSku> result = new ArrayList<>();
        for (OrderDetail od:odlist) {
            for (HotelSku sku:skuList) {
                if(od.getSkuId().equals(sku.getId().toString())){
                    if (sku.getNewPrice()!= null & sku.getNewPrice().intValue() != 0){
                        sku.setPrice(sku.getNewPrice());
                    }
                    sku.setNum(od.getNum());
                    result.add(sku);
                }
            }
        }
        //根据aid查找相关的地址信息
        Address address = addressMapper.selectAddressByID(order.getAddressId());
        map.put("skuList",result);
        map.put("address",address);
        return map;
    }
}
