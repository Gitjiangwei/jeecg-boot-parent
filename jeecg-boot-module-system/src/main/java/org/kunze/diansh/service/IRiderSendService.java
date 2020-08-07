package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.RiderSend;
import org.kunze.diansh.entity.Riders;

import java.util.List;

public interface IRiderSendService extends IService<RiderSend> {


    /**
     * 骑手派送添加
     *
     * **/

   Boolean saveRiderSend(RiderSend riderSend);

    /**
     *查看历史订单
     *
     * **/
    List<RiderSend> queryHitoryOrderList(RiderSend riderSend);

    /**
     * 修改骑手配送状态
     * **/
    int updateState(RiderSend riderSend);

    /**
     * 查询派送订单
     *
     * **/
    RiderSend queryRiderOrder(RiderSend riderSend);

    /**
     * 修改配送订单状态
     * **/
    int updateIsTurn(RiderSend riderSend);

    /**
     *
     *查看骑手是否空闲
     *
     * **/
    List<Riders>  queryRiderState(Riders riders);


}
