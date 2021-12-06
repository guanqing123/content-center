package com.itmuch.contentcenter.sentinel;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/6 23:06
 **/
public class FallbackClass {
    /**
     * 1.5 处理降级
     * - sentinel 1.6 可以处理Throwable
     * @Author guanqing
     * @Date 2021/12/6 22:58
     **/
    public static String fallback(String a) {
        return "限流,或者降级了 fallback";
    }
}
