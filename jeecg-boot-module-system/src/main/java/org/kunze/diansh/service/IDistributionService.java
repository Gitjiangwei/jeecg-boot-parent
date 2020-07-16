package org.kunze.diansh.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.kunze.diansh.controller.bo.RiderDistBo;
import org.kunze.diansh.controller.vo.RiderAndroidVo;
import org.kunze.diansh.controller.vo.RiderDistVo;
import org.kunze.diansh.entity.Distribution;

import java.util.List;

public interface IDistributionService extends IService<Distribution> {


    /**
     * 通知配送人员
     * @param orderId
     * @return
     */
    Boolean saveDistribution(String orderId,String deliveryFee);


    /**
     * 批量结算
     * @param ids
     * @return
     */
    Boolean editDistribution(String ids);


    /**
     * 后台查询配送信息
     * @param riderDistBo
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<RiderDistVo> qeruyDistList(RiderDistBo riderDistBo,Integer pageNo,Integer pageSize);


    /***
     * 骑手查询订单状态
     * @param orderId
     * @return
     */
    RiderAndroidVo queryRiderAndroid(String orderId);
}
