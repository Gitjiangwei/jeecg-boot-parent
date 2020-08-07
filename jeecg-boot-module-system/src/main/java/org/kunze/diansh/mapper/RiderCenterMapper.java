package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.RiderFlow;

import java.util.List;

@Mapper
public interface RiderCenterMapper extends BaseMapper<RiderFlow> {
    /***
     * 查询骑手流水信息
     * @param riderFlow
     * @return
     */
    List<RiderFlow> queryRiderFlowList(@Param("riderFlow")RiderFlow riderFlow);

    /***
     * 查询骑手资金管理--今日
     * @param riderFlow
     * @return
     */
    RiderFlow queryRiderTodayMoneySum(@Param("riderFlow")RiderFlow riderFlow);


    /***
     * 查询骑手资金管理--本月
     * @param riderFlow
     * @return
     */
    RiderFlow queryRiderMonthMoneySum(@Param("riderFlow")RiderFlow riderFlow);

    /***
     * 查询历史账单
     * @param riderFlow
     * @return
     */
    List<RiderFlow> queryRiderHistoryList(@Param("riderFlow")RiderFlow riderFlow);

    /**
     * 保存流水
     * **/
    int saveRiderFlow(@Param("riderFlow")RiderFlow riderFlow);

}
