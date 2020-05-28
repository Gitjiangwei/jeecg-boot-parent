package org.jeecg.common.util;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共工具类
 * @Backlight
 */
public class CommonUtil {

    /**
     * 格式化HashMap的Key值
     * 如：str_list 格式化为 strList
     * 从数据库字段转换为java驼峰命名格式
     * @param list
     * @return
     */
    public static List<Map<String, Object>> toCamel(List<Map<String, Object>> list){
        List newlist = new ArrayList();
        for(Map<String, Object> map:list){
            newlist.add(formatMapKey(map));
        }

        return newlist;

    }

    public static Map formatMapKey(Map<String, Object> map){

        Map<String, Object> hashMap = new HashMap<>();

        for(Map.Entry<String, Object> entry:map.entrySet()){
            hashMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey()), entry.getValue());

        }

        return hashMap;
    }

}
