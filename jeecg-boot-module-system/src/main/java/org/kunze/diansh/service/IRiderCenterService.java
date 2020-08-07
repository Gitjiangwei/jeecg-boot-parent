package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.RiderFlow;
import org.kunze.diansh.entity.RiderState;
import org.kunze.diansh.entity.Riders;

import java.util.List;

public interface IRiderCenterService extends IService<RiderFlow> {


    /***
     * 查询骑手流水
     * @param riderFlow
     * @return
     */
    List<RiderFlow> queryRiderFlowList(RiderFlow riderFlow);

    /**
     * 资金管理
     *
     * **/
    RiderFlow queryRiderMoneySum(RiderFlow riderFlow);


    /**
     * 查看骑手是否开启接单
     *
     * **/
    RiderState queryRiderState(RiderState riderState);


    /**
     * 修改骑手接单状态
     *
     * **/

    int updateRiderState(RiderState riderState);

    /**
     * 查看历史账单
     *
     * **/

    List<RiderFlow> queryRiderHistoryList(RiderFlow riderFlow);


    /**
     * 查看骑手信息
     *
     * **/
    Riders queryRiderInfo(Riders rider);




}
