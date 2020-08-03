package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.entity.RiderState;


@Mapper
public interface RiderCenterStateMapper extends BaseMapper<RiderState> {

    /***
     * 查询骑手是否开启接单
     * @param riderState
     * @return
     */
    RiderState queryRiderState(@Param("riderState") RiderState riderState);

    /***
     * 修改骑手接单状态
     * @param riderState
     * @return
     */
    int updateRiderState(@Param("riderState") RiderState riderState);



}
