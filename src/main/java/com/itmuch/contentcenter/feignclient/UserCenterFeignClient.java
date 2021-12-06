package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.config.UserContentFeignConfiguration;
import com.itmuch.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/1 22:13
 **/
@FeignClient(name = "user-center", fallback = UserCenterFeignClientFallback.class)
public interface UserCenterFeignClient {
    /** @FeignClient(name = "user-center", configuration = UserContentFeignConfiguration.class) */

    /**
     * http://user-center/users/{id}
     * @Author guanqing
     * @Date 2021/12/1 22:15
     **/
    @GetMapping("/users/{id}")
    String findUserById(@PathVariable String id);

}
