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

/**
 * 定时任务
 */
public class SchedulingTaskFeatures implements Job {

    @Autowired
    private SpuFeaturesMapper spuFeaturesMapper;



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

       //1、查询热卖表中的SkuId
        List<String> skuIds = spuFeaturesMapper.selectFeaturesSkuIds();
        if (skuIds != null && skuIds.size()>0){
            List<String> notSkuIds = spuFeaturesMapper.selectSkuNotState(skuIds);
            if(notSkuIds != null && notSkuIds.size()>0){
                spuFeaturesMapper.updateSkuFeatures(notSkuIds,"1");
            }
        }
        System.out.println("11111111111111111111111111111111111111111111");
    }
}
