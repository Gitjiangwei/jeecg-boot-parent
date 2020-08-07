package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.tencentSms.SendSms;
import org.jeecg.common.util.tencentSms.SendSmsEnum;
import org.kunze.diansh.controller.bo.RiderDistBo;
import org.kunze.diansh.controller.vo.RiderAndroidVo;
import org.kunze.diansh.controller.vo.RiderDistVo;
import org.kunze.diansh.controller.vo.RiderVo;
import org.kunze.diansh.controller.vo.ShopVo;
import org.kunze.diansh.entity.*;
import org.kunze.diansh.mapper.*;
import org.kunze.diansh.service.IDistributionService;
import org.kunze.diansh.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DistributionServiceImpl extends ServiceImpl<DistributionMapper, Distribution> implements IDistributionService {

    @Autowired
    private DistributionMapper distributionMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RiderMapper riderMapper;

    @Autowired
    private IOrderService orderService;

    /**
     * 通知配送人员
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean saveDistribution(String orderId,String deliveryFee,String distModel) {
        Boolean isFlag = false;
        //查询订单信息
        Order order = orderMapper.selectById(orderId);
        //地址信息
        Address address = addressMapper.selectAddressByID(order.getAddressId());
        //超市信息
        KzShop shop = new KzShop();
        shop.setId(order.getShopId());
        List<ShopVo> list = shopMapper.queryShopList(shop);
        if("1".equals(order.getPickUp())){
            String userName = address.getConsignee();
            if (address.getConsigneeSex()==0){
                userName = userName+"女士";
            }else if(address.getConsigneeSex()==1){
                userName = userName+"先生";
            }
            Boolean a =  SendSms.sendSms(address.getTelphone(),userName+","+list.get(0).getAddressTotal()+list.get(0).getShopName()+","+order.getPickNo(),SendSmsEnum.NOTICE_USER_SELF);
            if (!a) {
                System.out.println("短信发送失败！");
                return false;
            }else {
                return true;
            }
        }
        if (distModel.equals("1")){
            return true;
        }
        //骑手信息
        Rider rider = new Rider();
        rider.setArea(list.get(0).getArea());
        rider.setStatus("0");
        List<RiderVo> riderList = riderMapper.queryRiderList(rider);
        //如果是没有空闲的配送员执行
        if(riderList.size() == 0) {
            rider.setStatus("1");
            riderList = riderMapper.queryRiderList(rider);
        }else{
            int ran1 = 0;
            if(riderList.size()>1){
                Random r = new Random();
                ran1 = r.nextInt(riderList.size());
            }
            RiderVo riderVo = riderList.get(ran1);
            //添加配送信息
            Distribution distribution = new Distribution();
            distribution.setId(UUID.randomUUID().toString().replace("-","")); //主键ID
            distribution.setAddress(address.getProvince()+address.getCity()+address.getCounty()+address.getStreet()); //配送地址
            distribution.setOrderId(orderId); //订单号
            distribution.setRiderId(riderVo.getId()); //骑手id
            distribution.setShopId(shop.getId()); //超市id
            distribution.setDeliveryFee(CalculationUtil.MetaconversionScore(deliveryFee)); //配送费
            distribution.setPickNo(order.getPickNo()); //取单号
            riderMapper.editRiderNum("1",null,riderVo.getId());
            //发送短信
            Boolean a = SendSms.sendSms(riderVo.getTelphone(), orderId+","+list.get(0).getAddressTotal()+list.get(0).getShopName()+","+distribution.getPickNo(), SendSmsEnum.NOTIC_DISTRIBUTION_RIDER);
            if(!a){
                System.out.println("短信发送失败！");
                return false;
            }else {
                int result = distributionMapper.saveDistribution(distribution);
                if(result>0){
                    isFlag = true;
                }
            }
        }

        return isFlag;
    }

    /**
     * 批量结算
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean editDistribution(String ids) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(ids)){
            char a = ids.charAt(ids.length()-1);
            if(a == ','){
                ids = ids.substring(0,ids.length()-1);
            }
            if(ids == null || ids.equals("")){
                return false;
            }
            List<String> list = new ArrayList<String>();
            if(ids.contains(",")){
                list = new ArrayList<String>(Arrays.asList(ids.split(",")));
            }else{
                list.add(ids);
            }
            int result = distributionMapper.editDistribution(list);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /**
     * 后台查询配送信息
     *
     * @param riderDistBo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<RiderDistVo> qeruyDistList(RiderDistBo riderDistBo, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<RiderDistVo> riderDistVoList = distributionMapper.queryDistList(riderDistBo);
        List<RiderDistVo> riderDistVoLists = new ArrayList<RiderDistVo>();
        for(RiderDistVo item:riderDistVoList){
            RiderDistVo riderDistVo = new RiderDistVo();
            BeanUtils.copyProperties(item,riderDistVo);
            riderDistVo.setDeliveryFee(CalculationUtil.FractionalConversion(riderDistVo.getDeliveryFee()));
            riderDistVoLists.add(riderDistVo);
        }
        PageInfo<RiderDistVo> pageInfo = new PageInfo<RiderDistVo>(riderDistVoLists);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    /***
     * 骑手查询订单状态
     * @param orderId
     * @return
     */
    @Override
    public RiderAndroidVo queryRiderAndroid(String orderId) {
        if(StringUtils.isEmpty(orderId)){
            return null;
        }else {
            RiderAndroidVo riderAndroidVo = distributionMapper.queryRiderAndroid(orderId);
            riderAndroidVo.setDeliveryFee(CalculationUtil.FractionalConversion(riderAndroidVo.getDeliveryFee()));
            return riderAndroidVo;
        }

    }

    /***
     * 骑手修改配送状态
     * @param orderId
     * @return
     */
    @Override
    public Boolean editRiderOrderStatus(String orderId) {
        Boolean isFlag = false;
        RiderDistBo distBo = new RiderDistBo();
        distBo.setOrderId(orderId);
        List<RiderDistVo> riderDistVoList = distributionMapper.queryDistList(distBo);
        RiderDistVo riderDistVo = riderDistVoList.get(0);
        //1、修改订单状态
        String orderOk = orderService.updateOrderStatu("5",orderId);
        //2、修改骑手接单数
        String riderId = distributionMapper.queryByOrder(orderId);
        int result = riderMapper.editRiderNum("0",1,riderId);
        if(result>0){
            isFlag = true;
        }
        return isFlag;
    }
}
