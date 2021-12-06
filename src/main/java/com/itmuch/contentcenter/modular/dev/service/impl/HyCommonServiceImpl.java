package com.itmuch.contentcenter.modular.dev.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.itmuch.contentcenter.modular.dev.service.HyCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/6 14:48
 **/
@Slf4j
@Service
public class HyCommonServiceImpl implements HyCommonService {
    @Override
    @SentinelResource("common")
    public String common() {
        log.info("common....");
        return "common";
    }
}
