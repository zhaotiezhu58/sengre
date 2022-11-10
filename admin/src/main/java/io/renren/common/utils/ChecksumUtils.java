package io.renren.common.utils;

import cn.hutool.crypto.SecureUtil;

import java.util.*;

/**
 * 生成checksum工具类
 *
 * @author ssx
 */
public class ChecksumUtils {

    public static final String SECRET = "jshadjkhasjkdhasjkhdjashdlasjdljasdjaskjdkasj";


    public static String getChecksum(Map<String, String> paramsMap) {
        List paramList = sort(paramsMap);

        String paramString = "";
        Iterator itr = paramList.iterator();
        while (itr.hasNext()) {
            String key = itr.next().toString();
            String value = paramsMap.get(key);
            paramString += "&" + key + "=" + value;
        }
        String s = SecureUtil.md5(paramString);
        return s;
    }

    /**
     * 对Map类型数据按Key排序
     *
     * @param map
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List sort(Map map) {
        List list = new ArrayList(map.keySet());
        Collections.sort(list, (Comparator) (a, b) -> {
            //将key排在最后一位
            if (a.toString().equals("key")) {
                return 1;
            }
            if (b.toString().equals("key")) {
                return -1;
            }
            return a.toString().toLowerCase().compareTo(b.toString().toLowerCase());
        });
        return list;
    }

}
