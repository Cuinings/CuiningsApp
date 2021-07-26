package com.cn.cuinings.utils;

import java.util.List;
import java.util.Map;

/**
 * @Author: 崔宁 github: https://github.com/Cuinings
 * @Email: 1015597172@qq.com
 * @Date: 2021/7/26
 * @Description: 空判断
 */
public class EmptyUtil {

    public static boolean isEmpty(String str) {
        if (null == str || str.length() == 0)
            return true;
        return false;
    }

    public static boolean isEmpty(int[]... ints) {
        if (null == ints || ints.length == 0)
            return true;
        return false;
    }

    public static boolean isEmpty(Object[]... obj) {
        if (null == obj || obj.length == 0)
            return true;
        return false;
    }

    public static boolean isEmpty(List<?> list) {
        if (null == list || list.isEmpty())
            return true;
        return false;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        if (null == map || map.isEmpty())
            return true;
        return false;
    }
}
