package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Charge;

import java.util.Map;

public interface IChargeService extends IService<Charge> {

    /**
     * 添加手续费
     * @param serviceCharge
     * @return
     */
    Boolean saveCharge(String shopId,String serviceCharge);


    /***
     * 修改手续费
     * @param charge
     * @return
     */
    Boolean updateCharge(Charge charge);


    /***
     * 查询手续费
     * @return
     */
    Map<String,String> selectCharge(String shopId);
}
