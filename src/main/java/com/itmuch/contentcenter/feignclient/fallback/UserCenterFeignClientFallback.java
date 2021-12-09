package com.itmuch.contentcenter.feignclient.fallback;

import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/6 23:39
 **/
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public String findUserById(String id, String token) {
        return "一个默认用户";
    }
}
