package org.kunze.diansh.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.tencentSms.SendSms;
import org.jeecg.common.util.tencentSms.SendSmsEnum;
import org.kunze.diansh.controller.vo.RiderSendVo;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.*;
import org.kunze.diansh.service.IRiderSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
public class RiderSendServiceImpl extends ServiceImpl<RiderSendMapper, RiderSend> implements IRiderSendService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private  RiderSendMapper riderSendMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private RiderCenterStateMapper riderCenterStateMapper;
    @Autowired
    private RiderMapper riderMapper;
    @Autowired
    private RidersMapper ridersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private RiderCenterMapper riderCenterMapper;



    @Override
    public  Boolean saveRiderSend(RiderSend riderSend) {

        Boolean isFlag = false;
        RiderSendVo send=new RiderSendVo();
        //查看是否接单状态
        RiderState riderState=new RiderState();
        riderState.setRidersId(riderSend.getRiderId());
        RiderState riderStates=riderCenterStateMapper.queryRiderState(riderState);
        if(riderStates.getIsOpen()==1)
        {
            //查看订单
            Order order = orderMapper.selectById(riderSend.getOderId());
            //收货地址信息
            Address address = addressMapper.selectAddressByID(order.getAddressId());
            //商家信息
            ShopVo shopVo=riderSendMapper.queryShopInfo(order.getShopId());
            //骑手信息
            Rider rider = new Rider();
            rider.setArea(shopVo.getArea());
            rider.setStatus("0");
            List<RiderVo> riderList = riderMapper.queryRiderList(rider);
            //如果是没有空闲的配送员执行
            if(riderList.size() == 0) {
                rider.setStatus("1");
                riderList = riderMapper.queryRiderList(rider);
            }
            int ran1 = 0;
            if(riderList.size()>1){
                Random r = new Random();
                ran1 = r.nextInt(riderList.size());
            }
            RiderVo riderVo = riderList.get(ran1);
            //根据骑手ID 查询骑手名字
            Riders riders=new Riders();
            riders.setId(riderVo.getId());
            Riders ride=ridersMapper.queryRiderInfo(riders);

            //添加配送信息
            StringBuilder buf=new StringBuilder();
            buf.append(address.getProvince());
            buf.append(address.getCity());
            buf.append(address.getCounty());
            buf.append(address.getStreet());
            String addr=buf.toString();
            //主键
            send.setId(UUID.randomUUID().toString().replace("-",""));
            //骑手ID
            send.setRiderId(riderVo.getId());
            //骑手名字
            send.setRiderName(ride.getRiderName());
            //买家名字
            send.setBuyerName(address.getConsignee());
            //买家地址
            send.setBuyerAddress(addr);
            //买家电话
            send.setBuyerPhone(address.getTelphone());
            //商家地址
            send.setBusinessAddress(shopVo.getAddressTotal());
            //商家名称
            send.setBusinessName(shopVo.getShopName());
            //订单号
            send.setOderId(riderSend.getOderId());
            //订单状态
            send.setOrderState(0);
            //骑手状态
            send.setRiderState(0);
            //下单时间
            send.setOrderTime(order.getCreateTime());
            //配送费
            send.setSendPrice(riderSend.getSendPrice());
            //订单价格
            send.setOrderPrice(order.getPayment());
            //预计收入
            send.setIncome(riderSend.getSendPrice());
            //送达时间
            send.setSendTime("1");
            //超时时间
            send.setOutTime("0");
            //是否转派
            send.setIsTurn(0);
            //创建时间
            send.setCreateTime(new Date());
            //更多订单
            send.setOrderHistory(0);
            //距离 todo
            send.setDistance("5");
            //取货码
            send.setPickNo(order.getPickNo());
            //更新时间
            send.setUpdateTime(new Date());
            Boolean a = SendSms.sendSms(riderVo.getTelphone(), riderSend.getOderId()+","+shopVo.getAddressTotal()+shopVo.getShopName()+","+send.getPickNo(), SendSmsEnum.NOTIC_DISTRIBUTION_RIDER);
            if(!a){
                System.out.println("短信发送失败！");
                return isFlag;
            }else {
                int result = riderSendMapper.saveRiderSend(send);
                if(result>0){
                    isFlag = true;
                }
            }
        }


        return isFlag;
    }


    @Override
    public List<RiderSend> queryHitoryOrderList(RiderSend riderSend) {

        return  riderSendMapper.queryHitoryOrderList(riderSend);

    }

    @Override
    public int updateState(RiderSend riderSend) {
           if(riderSend.getRiderState()==1)
           {
               riderSend.setOrderState(1);
               return riderSendMapper.updateState(riderSend);
           }else if(riderSend.getRiderState()==2)
           {
               riderSend.setOrderState(2);
               return riderSendMapper.updateState(riderSend);
           }else if(riderSend.getRiderState()==3)
           {
               riderSend.setOrderState(3);
               int rider=riderSendMapper.updateState(riderSend);
               if(rider>0)
               {
                   RiderFlow flow=new RiderFlow();
                   flow.setRidersId(riderSend.getRiderId());
                   flow.setSingleNum(1);
                   flow.setOrderId(riderSend.getOderId());
                   flow.setSingleMoney(new BigDecimal(riderSend.getSendPrice()));
                   riderCenterMapper.saveRiderFlow(flow);
                   return rider ;
               }
           }
        return 0;
    }

    @Override
    public RiderSend queryRiderOrder(RiderSend riderSend) {

        RiderSend riders=riderSendMapper.queryRiderOrder(riderSend);
        //根据orderId查询订单详情
        List<OrderDetail> orderDetailList=orderDetailMapper.queryOrderDetail(riderSend.getOrderId());
        riders.setOrderDetail(orderDetailList);
        return riders;
    }

    @Override
    public int updateIsTurn(RiderSend riderSend) {
        if(riderSend.getIsTurn()==1)
        {
               riderSend.setOrderHistory(2);
        }

        return riderSendMapper.updateIsTurn(riderSend);
    }

    @Override
    public List<Riders> queryRiderState(Riders riders) {

             //查看是否有空闲骑手
             riders.setStatus("0");
             List<Riders> ridersList=riderSendMapper.queryRidersInfo(riders);
             if(ridersList!=null)
             {
                 return  ridersList;
             }else {
                 riders.setLevel(1);
                 return riderSendMapper.queryRidersInfo(riders);
             }

    }


}


