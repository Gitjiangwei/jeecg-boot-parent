package org.kunze.diansh.job;

import lombok.extern.slf4j.Slf4j;
import org.kunze.diansh.mapper.SpuFeaturesMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class SchedulingOverTask implements Job {


    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;


    /**
     * 监控每日特卖数据
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //1、查询热卖表中过期的SkuId
        List<String> skuIds = spuFeaturesMapper.selectFeaturesSkuId();
        if(skuIds != null && skuIds.size() > 0){
            //2、将过期的热卖商品Sku进行取消
            spuFeaturesMapper.updateSkuFeatures(skuIds,"0");
            //3、删除过期的热卖商品
            spuFeaturesMapper.delFeatures();
        }
        System.out.println("2222222222222222222222222222222222222222222");
    }
}
