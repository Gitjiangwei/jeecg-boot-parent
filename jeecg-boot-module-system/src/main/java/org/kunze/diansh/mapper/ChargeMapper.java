package org.kunze.diansh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.kunze.diansh.entity.Charge;

import java.util.Map;

public interface ChargeMapper extends BaseMapper<Charge> {

    /**
     * 添加手续费
     * @param charge
     * @return
     */
    int saveCharge(Charge charge);


    /**
     * 修改手续
     * @param charge
     * @return
     */
    int updateCharge(Charge charge);

    /**
     * 查询手续费
     * @return
     */
    Map<String,String> selectCharge(String shopId);
}
