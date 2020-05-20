package org.jeecg.modules.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.message.entity.SysUserShop;

import java.util.List;

public interface SysUserShopMapper extends BaseMapper<SysUserShop> {


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




    /***
     * 添加超市和用户中间表数据
     * @return
     */
    int saveSysUserShop(SysUserShop sysUserShop);


    /***
     * 修改超市和用户中间表数据
     * @return
     */
    int updateSysUserShop(SysUserShop sysUserShop);

    /***
     * 删除中间表数据
     * @param userId
     * @return
     */
    int delSysUserShop(@Param("userId") String userId);

    /***
     * 根据用户ID获取所属超市ID
     * @param userId
     * @return
     */
    String selectByUserId(@Param("userId") String userId);
}
