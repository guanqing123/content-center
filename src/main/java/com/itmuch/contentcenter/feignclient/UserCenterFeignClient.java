package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.config.UserContentFeignConfiguration;
import com.itmuch.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import com.itmuch.contentcenter.feignclient.fallbackfactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/1 22:13
 **/
@FeignClient(name = "user-center",
        // fallback = UserCenterFeignClientFallback.class
        fallbackFactory = UserCenterFeignClientFallbackFactory.class)
public interface UserCenterFeignClient {
    /** @FeignClient(name = "user-center", configuration = UserContentFeignConfiguration.class) */

    /**
     * http://user-center/users/{id}
     * @RequestHeader("Authorization") feign会传出token到请求头上
     * @Author guanqing
     * @Date 2021/12/1 22:15
     **/
    @GetMapping("/users/{id}")
    String findUserById(@PathVariable String id);
}
