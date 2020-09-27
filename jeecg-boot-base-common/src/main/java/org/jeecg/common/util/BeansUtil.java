package org.jeecg.common.util;

import cn.hutool.core.bean.BeanUtil;
import org.jeecg.common.exception.JeecgBootException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合复制工具类
 *
 * @author 姜伟
 * @date 2020/7/24
 */
public class BeansUtil {
    private BeansUtil() {
    }

    public static < T1, T2> List< T2 > listCopy(List< T1 > sourceList, Class< T2 > clazz) {
        return (List) sourceList.stream().map((source) -> {
            Object target;
            try {
                target = clazz.getDeclaredConstructor().newInstance();
            }
            catch (Exception e) {
                throw new JeecgBootException("集合拷贝失败");
            }
            BeanUtil.copyProperties(source, target);
            return target;
        }).collect(Collectors.toList());
    }
}
