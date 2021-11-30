package com.itmuch.contentcenter.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonConfig.RibbonConfig;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/29 22:54
 **/
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfig.class)
public class UserCenterRibbonConfig {
}
