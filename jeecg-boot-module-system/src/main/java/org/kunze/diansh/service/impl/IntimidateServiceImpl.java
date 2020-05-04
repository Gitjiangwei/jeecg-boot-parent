package org.kunze.diansh.service.impl;

import org.kunze.diansh.controller.vo.DistributionVo;
import org.kunze.diansh.controller.vo.SalesTicketVo;
import org.kunze.diansh.entity.Address;
import org.kunze.diansh.entity.Commodity;
import org.kunze.diansh.entity.Order;
import org.kunze.diansh.entity.OrderDetail;
import org.kunze.diansh.mapper.AddressMapper;
import org.kunze.diansh.mapper.OrderMapper;
import org.kunze.diansh.service.IIntimidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntimidateServiceImpl implements IIntimidateService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;
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
        //5、添加配送信息
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
            //5.1、根据pickUp来判断是商家配送还是自提
            salesTicketVo.setPickUp("商家配送");
            String addres = address.getProvince()+address.getCity()+address.getCounty()+address.getStreet();
            distributionVo.setShippingAddress(addres);
        }else {
            salesTicketVo.setPickUp("自提");
            distributionVo.setShippingAddress("");
        }
        salesTicketVo.setDistributionVo(distributionVo);
        //6、填充商品信息
        List<Commodity> commodities = new ArrayList<>();

        return null;
    }
}
