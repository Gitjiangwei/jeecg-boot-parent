package org.jeecg.common.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 公告工具类
 *
 * @author 姜伟
 * @date 2020/9/20
 */
public class CommonUtil {

    /**
     * 逗号
     */
    private static final char CHAR_COMMA = ',';

    /**
     * 格式化HashMap的Key值 如：str_list 格式化为 strList 从数据库字段转换为java驼峰命名格式
     * 
     * @param list 数据集
     * @return 返回格式化后的集合
     */
    public static List< Map< String, Object > > toCamel(List< Map< String, Object > > list) {
        List< Map< String, Object > > mapArrayList = new ArrayList<>();
        for (Map< String, Object > map : list) {
            mapArrayList.add(formatMapKey(map));
        }
        return mapArrayList;

    }

    /**
     *
     *
     * @param map 参数值
     * @return 返回集合
     * @author 姜伟
     * @date 2020/9/20 19:46
     */
    public static Map< String, Object > formatMapKey(Map< String, Object > map) {
        Map< String, Object > hashMap = new HashMap<>(10);
        for (Map.Entry< String, Object > entry : map.entrySet()) {
            hashMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey()), entry.getValue());

        }
        return hashMap;
    }

    /**
     * 逗号分隔字符串拆分List集合
     *
     * @param str 字符串
     * @return 拆分后的数据列
     * @author 姜伟
     * @date 2020/9/20 18:58
     */
    public static List< String > commaSeparatedStringList(String str) {
        char comma = str.charAt(str.length() - 1);
        if (comma == CHAR_COMMA) {
            str = str.substring(0, str.length() - 1);
        }
        if (StringUtils.isBlank(str)) {
            throw new NullPointerException(CommonConstant.NULL_PARAMETER);
        }
        List< String > stringList = new ArrayList<>();
        if (str.contains(String.valueOf(CHAR_COMMA))) {
            stringList = new ArrayList<>(Arrays.asList(str.split(",")));
        }
        else {
            stringList.add(str);
        }
        return stringList;
    }

    /**
     * 获取当前操作用户名
     *
     * @return
     * @author 姜伟
     * @date 2020/9/20 19:52
     */
    public static String getUserName(){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String updateName = "";
        if (sysUser != null) {
            updateName = sysUser.getRealname();
        }
        return updateName;
    }
}
