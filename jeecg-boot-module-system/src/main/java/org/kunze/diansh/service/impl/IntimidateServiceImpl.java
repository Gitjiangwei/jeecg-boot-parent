package org.kunze.diansh.service.impl;

import org.jeecg.common.util.CalculationUtil;
import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.SalesTicketVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.OrderRecordMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IIntimidateService;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IntimidateServiceImpl implements IIntimidateService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;
    /***
     * 根据订单ID获取打印小票的数据
     * @param orderId 订单id
     * @return
     */
    @Override
    public SalesTicketVo selectSales(String orderId,String status) {
        if(orderId == null || orderId.equals("")){
            return null;
        }
        SalesTicketVo salesTicketVo = new SalesTicketVo();
        //1、根据订单ID查询到订单信息
        Order order = orderMapper.selectById(orderId);
        //2、根据订单信息中pickUp来判断是自提还是商家配送
        //3、根据订单信息中的地址id获取用户地址查询配送地址
        Address address = addressMapper.selectAddressByID(order.getAddressId());
        //4、根据订单id来获取详细订单中商品信息
        List<OrderDetail> orderDetails = orderMapper.selectOrderDetailById(orderId);
        //5、根据订单信息中的超市id查询超市信息
        KzShop shop = shopMapper.selectByKey(order.getShopId());

        //6、添加配送信息
        DistributionVo distributionVo = new DistributionVo();
        String sex = "";
        if(address.getConsigneeSex()== 1){
            sex = "先生";
        }else if(address.getConsigneeSex() == 0){
            sex = "女士";
        }
        String call = address.getConsignee()+sex;
        distributionVo.setCall(call);
        String tel = address.getTelphone()==null?"":address.getTelphone();
/*        if(!tel.equals("")) {
            tel = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
            distributionVo.setContact(tel);
        }else {
            distributionVo.setContact(tel);
        }*/
        distributionVo.setContact(tel);

        if(order.getPickUp().equals("2")){
            //6.1、根据pickUp来判断是商家配送还是自提
            salesTicketVo.setPickUp("商家配送");
            String addres = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet();
            distributionVo.setShippingAddress(addres);
        }else {
            salesTicketVo.setPickUp("自提");
            String addres = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet();
            distributionVo.setShippingAddress(addres);
        }
        salesTicketVo.setDistributionVo(distributionVo);
        //7、填充商品信息
        List<Commodity> commodities = new ArrayList<>();
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
            Commodity commodity = new Commodity(title,price,num+"*"+price,totalPrice.toString());
            commodities.add(commodity);
        }
        salesTicketVo.setCommodityList((ArrayList<Commodity>)commodities);
        //8、填充订单其它信息
        salesTicketVo.setOrders(orderId); //订单编号
        salesTicketVo.setShopName(shop.getShopName()); //超市名称
        salesTicketVo.setShopAddress(shop.getProvince()+shop.getCity()+shop.getArea()+shop.getShopAddress()); //超市地址
        salesTicketVo.setSaleNum(totalNum.toString());//商品总数
        salesTicketVo.setBuyerMessage(order.getBuyerMessage()==null?"":order.getBuyerMessage());
        salesTicketVo.setSaleSum(CalculationUtil.FractionalConversion(order.getAmountPayment())); //应付金额
        salesTicketVo.setPractical(CalculationUtil.FractionalConversion(order.getPayment()));//实付金额
        salesTicketVo.setPostFree(CalculationUtil.FractionalConversion(order.getPostFree()));//配送费
        //salesTicketVo.setPriceTotle(new BigDecimal(salesTicketVo.getPostFree()).add(new BigDecimal(salesTicketVo.getPractical())).toString());
        if(status.equals("2")) {
            orderService.updateOrderStatu("3", orderId);
        }
        return salesTicketVo;
    }
}
