package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.DateUtilTest;
import org.kunze.diansh.entity.RiderFlow;
import org.kunze.diansh.entity.RiderState;
import org.kunze.diansh.entity.Riders;
import org.kunze.diansh.mapper.RiderCenterMapper;
import org.kunze.diansh.mapper.RiderCenterStateMapper;
import org.kunze.diansh.mapper.RidersMapper;
import org.kunze.diansh.service.IRiderCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service
public class RiderCenterServiceImpl extends ServiceImpl<RiderCenterMapper, RiderFlow> implements IRiderCenterService {

    @Autowired
    private RiderCenterMapper riderCenterMapper;

    @Autowired
    private RiderCenterStateMapper riderCenterStateMapper;

    @Autowired
    private RidersMapper ridersMapper;


    @Override
    public List<RiderFlow> queryRiderFlowList(RiderFlow riderFlow) {
        List<RiderFlow> riderVoList = riderCenterMapper.queryRiderFlowList(riderFlow);
        return riderVoList;

    }

    @Override
    public RiderFlow queryRiderMoneySum(RiderFlow riderFlow) {

        if(riderFlow.getParamType()==1)
        {
            String startTime=DateUtilTest.getBeginOfDay();
            String endTime=DateUtilTest.getEndOfDay();
            riderFlow.setStartTime(startTime);
            riderFlow.setEndTime(endTime);
            RiderFlow riderVoList = riderCenterMapper.queryRiderTodayMoneySum(riderFlow);
            return riderVoList;
        }else {
            String startTime=DateUtilTest.getBeginMonth();
            String endTime=DateUtilTest.getEndMonth();
            riderFlow.setStartTime(startTime);
            riderFlow.setEndTime(endTime);
            RiderFlow riderVoList = riderCenterMapper.queryRiderMonthMoneySum(riderFlow);
            return riderVoList;
        }

    }

    @Override
    public RiderState queryRiderState(RiderState riderState) {

        return riderCenterStateMapper.queryRiderState(riderState);
    }

    @Override
    @Transactional
    public int updateRiderState(RiderState riderState) {
        try {
            return  riderCenterStateMapper.updateRiderState(riderState);
        }catch (Exception e){
            log.error("插入失败");
            return 0;
        }

    }

    @Override
    public List<RiderFlow> queryRiderHistoryList(RiderFlow riderFlow) {
          List<RiderFlow> riderVoList = riderCenterMapper.queryRiderHistoryList(riderFlow);
         return riderVoList;
    }

    @Override
    public Riders queryRiderInfo(Riders rider) {

        return ridersMapper.queryRiderInfo(rider);
    }
}
