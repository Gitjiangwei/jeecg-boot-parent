package org.kunze.diansh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kunze.diansh.entity.Charge;

public interface IChargeService extends IService<Charge> {

    /**
     * 添加手续费
     * @param serviceCharge
     * @return
     */
    Boolean saveCharge(String serviceCharge);


    /***
     * 修改手续费
     * @param charge
     * @return
     */
    Boolean updateCharge(Charge charge);
}
