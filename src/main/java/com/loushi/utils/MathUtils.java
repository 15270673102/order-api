package com.loushi.utils;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

public class MathUtils {


    /**
     * 任务号
     */
    public static String taskNum() {
        return StrUtil.format("T-{}", IdUtil.simpleUUID());
    }

    /**
     * 订单
     */
    public static String orderNum() {
        return "O-" + IdUtil.simpleUUID();
    }

    /**
     * 流水
     */
    public static String payNum() {
        return "P-" + IdUtil.simpleUUID();
    }

}
