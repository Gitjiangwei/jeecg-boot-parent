package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.kunze.diansh.entity.Charge;
import org.kunze.diansh.mapper.ChargeMapper;
import org.kunze.diansh.service.ICategoryService;
import org.kunze.diansh.service.IChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements IChargeService {


    @Autowired
    private ChargeMapper chargeMapper;


    /**
     * 添加手续费
     *
     * @param serviceCharge
     * @return
     */
    @Override
    public Boolean saveCharge(String shopId,String serviceCharge) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(serviceCharge) && !StringUtils.isEmpty(shopId)){
            Charge charge = new Charge();
            charge.setId(UUID.randomUUID().toString().replace("-",""));
            charge.setServiceCharge(serviceCharge);
            charge.setShopId(shopId);
            int result = chargeMapper.saveCharge(charge);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /***
     * 修改手续费
     * @param charge
     * @return
     */
    @Override
    public Boolean updateCharge(Charge charge) {
        Boolean isFlag = false;
        if(!StringUtils.isEmpty(charge.getId())){
            int result = chargeMapper.updateCharge(charge);
            if(result>0){
                isFlag = true;
            }
        }
        return isFlag;
    }

    /***
     * 查询手续费
     * @return
     */
    @Override
    public Map<String, String> selectCharge(String shopId) {
        return chargeMapper.selectCharge(shopId);
    }
}
