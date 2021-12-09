package com.itmuch.contentcenter.modular.dev.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/9 21:47
 **/
@RefreshScope
@RestController
@RequestMapping("/content")
public class ConfigController {

    @Value("${your.configuration}")
    private String yourConfiguration;

    @GetMapping("/config")
    public String testConfiguration() {
        return yourConfiguration;
    }
}
