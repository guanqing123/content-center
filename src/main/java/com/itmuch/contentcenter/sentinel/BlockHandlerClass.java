package com.itmuch.contentcenter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/6 23:04
 **/
@Slf4j
public class BlockHandlerClass {
    /**
     * 处理限流或者降级
     * @Author guanqing
     * @Date 2021/12/6 22:58
     **/
    public static String block(String a, BlockException e) {
        log.warn("限流,或者降级了 block", e);
        return "限流,或者降级了 block";
    }
}
