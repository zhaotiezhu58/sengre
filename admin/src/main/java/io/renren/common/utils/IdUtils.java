package io.renren.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 生成各类业务订单ID
 */
public class IdUtils {

    public static String getTopUp() {
        return "t" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getWithdraw() {
        return "w" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getDaifu() {
        return "df" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getBill() {
        return "b" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getGrabOrderNo() {
        return "g" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String getGrabOrderDetailNo() {
        return "gd" + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String randomId(String bill) {
        return bill + Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }

    public static String randomId() {
        return Long.toString(IdUtil.getSnowflake(1, 1).nextId());
    }
}
