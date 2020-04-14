package org.jeecg.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

    /**
     * 得到中文首字母
     * @param str
     * @param isToCase true：转换成大写，false不转换大写
     * @return
     */
    public static String getPinYinHeadChar(String str,boolean isToCase){
        String convert = "";
        for(int i = 0; i<str.length(); i++){
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if(pinyinArray != null){
                convert += pinyinArray[0].charAt(0);
            }else{
                convert += word;
            }
        }
        if(isToCase){
           convert =  convert.toUpperCase();
        }
        return convert;
    }


    /**
     * 得到中文全拼
     * @param str
     * @param isToCase true：转换成大写，false不转换大写
     * @return
     */
    public static  String getPinYin(String str,Boolean isToCase){
        char[] t1 = str.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for(int i = 0; i < t0; i++){
                //判断是否为汉子字符
                if(Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")){
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i],t3);
                    t4 += t2[0];
                }else {
                    t4 += Character.toString(t1[i]);
                }
            }
        }catch (BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        if(isToCase){
          t4 =  t4.toUpperCase();
        }
        return t4;
    }


    public static void main(String[] args){
        String str = "空调";
        System.out.println("首字母："+getPinYinHeadChar(str,true));
        System.out.println("全拼："+getPinYin(str,true));
    }
}
