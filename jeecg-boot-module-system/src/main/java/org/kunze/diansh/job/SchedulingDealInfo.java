package org.kunze.diansh.job;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.DealInfo;
import org.kunze.diansh.mapper.DealInfoMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 定时记录商铺汇总信息
 */
@Slf4j
public class SchedulingDealInfo implements Job {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private DealInfoMapper dealInfoMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> ids = shopMapper.selectShopIds();
        if(EmptyUtils.isEmpty(ids)){
            log.error("记录商铺汇总信息失败！商品为空");
            return;
        }
        for (String id:ids) {
            Map map = shopMapper.selectOldMonthMoney(id); //查询上个月的汇总数据
            String payment = map.get("payment").toString();//订单交易额
            String postFree = map.get("postFree").toString();//配送费
            String okPayment = map.get("okPayment").toString();//交易完成的钱
            String okTotal = map.get("okTotal").toString();//交易完成的单量
            String refundPayment = map.get("refundPayment").toString();//退款金额
            String refundTotal = map.get("refundTotal").toString(); //退款单量
            String serviceFee = NumberUtil.mul(NumberUtil.add(payment,postFree),new BigDecimal("0.01")).toString(); //手续费 (订单交易额+配送费*0.01)
            String totalPayment = NumberUtil.sub(NumberUtil.add(okPayment,refundPayment,postFree),new BigDecimal(serviceFee)).toString(); //月总交易额 (交易完成的钱+退款金额+配送费 - 服务费)

            DealInfo dealInfo = DealInfo.builder()
                    .id(UUID.randomUUID().toString().replace("-",""))
                    .payment(payment)
                    .postFree(postFree)
                    .okPayment(okPayment)
                    .okTotal(okTotal)
                    .refundPayment(refundPayment)
                    .refundTotal(refundTotal)
                    .totalPayment(totalPayment)
                    .serviceFee(serviceFee)
                    .createTime(new Date())
                    .build();
            dealInfoMapper.insert(dealInfo);
        }
    }
}