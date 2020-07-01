package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.CalculationUtil;
import org.kunze.diansh.controller.bo.DealInfoBo;
import org.kunze.diansh.controller.vo.DealInfoVo;
import org.kunze.diansh.entity.DealInfo;
import org.kunze.diansh.entity.modelData.DealInfoModel;
import org.kunze.diansh.mapper.DealInfoMapper;
import org.kunze.diansh.service.IDealInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DealInfoServiceImpl extends ServiceImpl<DealInfoMapper, DealInfo> implements IDealInfoService {


    @Autowired
    private DealInfoMapper dealInfoMapper;


    /**
     * 商家查询历史交易清单
     *
     * @param dealInfoBo
     * @return
     */
    @Override
    public PageInfo<DealInfoVo> queryDealInfoList(DealInfoBo dealInfoBo,Integer pageNo,Integer pageSize) {
        Page page = PageHelper.startPage(pageNo,pageSize);
        List<DealInfoModel> dealInfoModels = dealInfoMapper.queryDealInfoList(dealInfoBo);
        List<DealInfoVo> newDealInfoVos = new ArrayList<DealInfoVo>();
        for(DealInfoModel item:dealInfoModels){
            DealInfoVo dealInfoVo = new DealInfoVo();
            dealInfoVo.setId(item.getId());
            dealInfoVo.setServiceChange(item.getServiceChange()+"%");
            dealInfoVo.setOccurrenceTime(item.getOccurrenceTime());
            dealInfoVo.setOkPaymentTotal(item.getOkTotal()+"单"+ CalculationUtil.FractionalConversion(item.getOkPayment())+"元");
            dealInfoVo.setRefundPaymentTotal(item.getRefundTotal()+"单"+CalculationUtil.FractionalConversion(item.getRefundPayment())+"元");
            dealInfoVo.setPayment(CalculationUtil.FractionalConversion(item.getPayment()));
            dealInfoVo.setTotalPayment(CalculationUtil.FractionalConversion(item.getTotalPayment()));
            dealInfoVo.setServiceFee(CalculationUtil.FractionalConversion(item.getServiceFee()));
            newDealInfoVos.add(dealInfoVo);
        }
        PageInfo<DealInfoVo> infoVoPageInfo = new PageInfo<DealInfoVo>(newDealInfoVos);
        infoVoPageInfo.setTotal(page.getTotal());
        return infoVoPageInfo;
    }
}
