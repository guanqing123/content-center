package com.itmuch.contentcenter.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * feign的配置类 (父子上下文)
 * 这个类别加@Configuration注解了,否则必须挪到@ComponentScan能扫描的包以外
 * @Author guanqing
 * @Date 2021/12/5 18:59
 **/
public class GlobalFeignConfiguration {
    @Bean
    public Logger.Level level() {
        /** 让feign打印所有请求的细节... */
        return Logger.Level.FULL;
    }
}
