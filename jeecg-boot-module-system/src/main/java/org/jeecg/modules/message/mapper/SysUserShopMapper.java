package org.jeecg.modules.message.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserShopMapper {


    /***
     * 标记在线状态
     * @param userId
     * @return
     */
    int updateOnline(@Param("userId") String userId,@Param("status") String status);


    /**
     * 根据超市Id获取当前超市在线的接收订单的用户Id
     * @param ids
     * @return
     */
    List<String> selectByIds(@Param("shopId") String ids);
}
