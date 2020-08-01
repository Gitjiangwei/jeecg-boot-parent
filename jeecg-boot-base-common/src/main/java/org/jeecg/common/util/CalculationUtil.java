package org.jeecg.common.util;

import java.math.BigDecimal;

/**
 * 分转换为元，元转换分
 */
public class CalculationUtil {


    /***
     * 分转换元
     * @return
     */
    public static String FractionalConversion(String ordPrice){
        if(ordPrice==null||ordPrice.equals("")){
            return "0";
        }else {
            BigDecimal old = new BigDecimal(ordPrice);
            BigDecimal newPrice = old.divide(new BigDecimal("100"));
            return newPrice.setScale(2).toString();
        }
    }

    /***
     * 元转换分
     * @return
     */
    public static String MetaconversionScore(String ordPrice){
        if(ordPrice==null||ordPrice.equals("")){
            return "0";
        }else {
            BigDecimal old = new BigDecimal(ordPrice);
            BigDecimal newPrice = old.multiply(new BigDecimal("100"));
            return newPrice.toString();
        }
    }

    /***
     * 计算手续手续费率（整数转换小数）
     * @return
     */
    public static String ServiceCharge(String charge){
        if(charge == null || charge.equals("")){
            return "0";
        }else {
            BigDecimal serviceCharge = new BigDecimal(charge).divide(new BigDecimal("100"));
            return serviceCharge.setScale(3).toString();
        }
    }
}
