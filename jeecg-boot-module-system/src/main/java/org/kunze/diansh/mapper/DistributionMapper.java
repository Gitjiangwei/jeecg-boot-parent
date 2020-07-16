package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kunze.diansh.controller.bo.RiderDistBo;
import org.kunze.diansh.controller.vo.RiderAndroidVo;
import org.kunze.diansh.controller.vo.RiderDistVo;
import org.kunze.diansh.entity.Distribution;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DistributionMapper extends BaseMapper<Distribution> {

    /**
     * 添加配送信息
     * @param distribution
     * @return
     */
    int saveDistribution(Distribution distribution);

    /***
     * 结算
     * jw
     * @param id
     * @return
     */
    int editDistribution(@Param("ids") List<String> ids);


    /***
     * 后台查询配送信息
     * @param riderDistBo
     * @return
     */
    List<RiderDistVo> queryDistList(RiderDistBo riderDistBo);


    /***
     * 骑手查询订单
     * @param orderId
     * @return
     */
    RiderAndroidVo queryRiderAndroid(@RequestParam("orderId") String orderId);
}
