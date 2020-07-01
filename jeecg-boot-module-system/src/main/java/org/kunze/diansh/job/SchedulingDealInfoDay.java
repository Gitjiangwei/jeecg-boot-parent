package org.kunze.diansh.job;

import cn.hutool.core.util.NumberUtil;
import org.jeecg.common.util.CalculationUtil;
import org.jeecg.common.util.EmptyUtils;
import org.kunze.diansh.entity.DealInfo;
import org.kunze.diansh.mapper.DealInfoMapper;
import org.kunze.diansh.mapper.ShopMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SchedulingDealInfoDay implements Job {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private DealInfoMapper dealInfoMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Map<String,String>> dayList = shopMapper.selectDaysMoney();
        for (Map map:dayList) {
            String shopId = map.get("shopId").toString();
            String charge = map.get("charge").toString(); //服务费率
            String payment = map.get("payment").toString();//订单交易额
            String postFree = map.get("postFree").toString();//配送费
            String okPayment = map.get("okPayment").toString();//交易完成的钱
            String okTotal = map.get("okTotal").toString();//交易完成的单量
            String refundPayment = map.get("refundPayment").toString();//退款金额
            String refundTotal = map.get("refundTotal").toString(); //退款单量
            String serviceFee = "0";
            if (!charge.equals("0")) {
                serviceFee = NumberUtil.round(NumberUtil.mul(payment, CalculationUtil.ServiceCharge(charge)).toString(), 0, RoundingMode.UP).toString(); //手续费 (订单交易额*0.01)
            }
            String totalPayment = NumberUtil.sub(NumberUtil.add(okPayment, refundPayment), new BigDecimal(serviceFee)).toString(); //月总交易额 (交易完成的钱+退款金额 - 服务费)

            DealInfo dealInfo = DealInfo.builder()
                    .id(UUID.randomUUID().toString().replace("-", ""))
                    .payment(payment)
                    .postFree(postFree)
                    .okPayment(okPayment)
                    .okTotal(okTotal)
                    .refundPayment(refundPayment)
                    .refundTotal(refundTotal)
                    .totalPayment(totalPayment)
                    .serviceFee(serviceFee)
                    .createTime(new Date())
                    .shopId(shopId)
                    .serviceChange(charge)
                    .dateFlag(1)
                    .build();
            dealInfoMapper.insert(dealInfo);
        }
    }
}
