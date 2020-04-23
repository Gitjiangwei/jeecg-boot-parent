package org.jeecg.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成订单编号工具类
 */
public class OrderCodeUtils {

    private static final int todayNum = 6;

    /**
     * 订单编号规范
     * @param shopName 当前超市订单的总数量
     * @param shopOrdertodayNum 当前超市当天的订单数
     * @return
     */

    public static String orderCode(String shopName,String shopOrdertodayNum){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHMMSS");
        String date = format.format(new Date(System.currentTimeMillis()));
        String numberCount = "";
        String totalCount = "";
        String orderCode = "";
        int a = Integer.valueOf(shopOrdertodayNum);
        int todaynum = todayNum - shopOrdertodayNum.length();
        String pinyinName = PinyinUtil.getPinYinHeadChar(shopName,true);
        if(a>0) {
            shopOrdertodayNum = String.valueOf(a+1);
            for (int i = 0; i < todaynum; i++) {
                numberCount = "0" + numberCount;
            }

            orderCode = date + totalCount + change(pinyinName) + numberCount + shopOrdertodayNum;

        }else {
            orderCode = date + totalCount + change(pinyinName) + numberCount + "000001";
        }

        return orderCode;
    }

    /**
     * 将超市的首字母拼音转换成ASCII码进行输出
     * @param pinyinName
     * @return
     */
    private static String change(String pinyinName){
        String py  = "";
        int pyLength = pinyinName.length();
        //从超市名称首字母中随机取出2位字母进行转换
        char[] array = pinyinName.toCharArray();
        String newPyName = "";
        if(array.length>2) {
            for (int i = 0; i < 2; i++) {
                int ranNum = (int) (Math.random() * pyLength);
                newPyName += String.valueOf(array[ranNum]);
            }
        }else {
            newPyName = pinyinName;
        }
        for(int i = 0; i< newPyName.length(); i++){
            char c = newPyName.charAt(i);
            if( c >= 'A' && c <= 'Z'){
                c += 32;
                byte byteAscii = (byte)c;
                py = py + byteAscii;
            }
        }
        return py;
    }

    public static void main(String[] args){
        System.out.println(orderCode("领导","0"));
    }


}
