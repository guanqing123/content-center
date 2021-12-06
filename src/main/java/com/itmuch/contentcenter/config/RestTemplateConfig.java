package com.itmuch.contentcenter.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/28 20:27
 **/
@Configuration
public class RestTemplateConfig {

    /** 在spring容器中,创建一个对象,类型RestTemplate;名称/ID */
    /** <bean id="restTemplate" class="org.springframework.web.client.RestTemplate" /> */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
