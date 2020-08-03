package org.jeecg.common.util;

import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class DateUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);


    //当天的开始时间
    public static  String getBeginOfDay()
    {
        String dateStr = DateUtil.formatTime(new Date());
        Date date = DateUtil.parse(dateStr);
        String beginOfDay = DateUtil.beginOfDay(date).toString();
        LOGGER.info(beginOfDay);
        return beginOfDay;
    }
    //当天的结束时间
    public  static  String getEndOfDay()
    {
        String dateStr = DateUtil.formatTime(new Date());
        Date date = DateUtil.parse(dateStr);
        String endOfDay = DateUtil.endOfDay(date).toString();
        LOGGER.info(endOfDay);
        return endOfDay;
    }

    //当月第一天
    public  static  String getBeginMonth()
    {
        String beginMonth=DateUtil.format(DateUtil.beginOfMonth(new Date()),"yyyy-MM-dd") ;
        LOGGER.info(beginMonth);
        return beginMonth;
    }

    //当月最后一天
    public  static  String getEndMonth()
    {
        String endMonth=DateUtil.format(DateUtil.endOfMonth(new Date()),"yyyy-MM-dd");
        LOGGER.info(endMonth);
        return endMonth;
    }



    public static void main(String[] args) {

        String a=DateUtilTest.getBeginOfDay();
        String a1=DateUtilTest.getEndOfDay();
        System.out.println(a);
        System.out.println(a1);

        String b=DateUtilTest.getBeginMonth();
        String b1=DateUtilTest.getEndMonth();
        System.out.println(b);
        System.out.println(b1);


    }










}
