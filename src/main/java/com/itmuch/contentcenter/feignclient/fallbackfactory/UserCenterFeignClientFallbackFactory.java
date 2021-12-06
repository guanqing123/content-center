package com.itmuch.contentcenter.feignclient.fallbackfactory;

import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/6 23:48
 **/
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient() {
            @Override
            public String findUserById(String id) {
                log.warn("远程调用被限流/降级了", throwable);
                return "一个默认的用户-fallbackfactory";
            }
        };
    }
}
