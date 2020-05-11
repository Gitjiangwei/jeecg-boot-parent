package org.kunze.diansh.service.impl;

import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.SalesTicketVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.kunze.diansh.service.IIntimidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IntimidateServiceImpl implements IIntimidateService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ShopMapper shopMapper;
    /***
     * 根据订单ID获取打印小票的数据
     * @param orderId 订单id
     * @return
     */
    @Override
    public SalesTicketVo selectSales(String orderId) {
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
        if(address.getConsigneeSex()== 0){
            sex = "先生";
        }else if(address.getConsigneeSex() == 1){
            sex = "女士";
        }
        String call = address.getConsignee()+sex;
        distributionVo.setCall(call);
        distributionVo.setContact(address.getTelphone()==null?"":address.getTelphone());
        if(order.getPickUp().equals("2")){
            //6.1、根据pickUp来判断是商家配送还是自提
            salesTicketVo.setPickUp("商家配送");
            String addres = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet();
            distributionVo.setShippingAddress(addres);
        }else {
            salesTicketVo.setPickUp("自提");
            distributionVo.setShippingAddress("");
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
            Commodity commodity = new Commodity(title,price,num,totalPrice.toString());
            commodities.add(commodity);
        }
        salesTicketVo.setCommodityList((ArrayList<Commodity>)commodities);
        //8、填充订单其它信息
        salesTicketVo.setOrders(orderId); //订单编号
        salesTicketVo.setShopName(shop.getShopName()); //超市名称
        salesTicketVo.setShopAddress(shop.getShopAddress()); //超市地址
        salesTicketVo.setSaleNum(totalNum.toString());//商品总数
        BigDecimal amout = new BigDecimal(order.getAmountPayment());
        amout = amout.multiply(new BigDecimal("0.01"));
        salesTicketVo.setSaleSum(amout.toString()); //应付金额
        BigDecimal payAmout = new BigDecimal(order.getPayment());
        payAmout = payAmout.multiply(new BigDecimal("0.01"));
        salesTicketVo.setPractical(payAmout.toString());//实付金额
        return salesTicketVo;
    }
}
