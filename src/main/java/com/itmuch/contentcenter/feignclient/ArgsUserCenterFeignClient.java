package com.itmuch.contentcenter.feignclient;

import com.itmuch.contentcenter.modular.dev.model.param.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: TODO 多参数
 * @Author guanqing
 * @Date 2021/12/5 20:30
 **/
@FeignClient(name = "user-center")
public interface ArgsUserCenterFeignClient {
    @GetMapping("/users/search")
    String query(@SpringQueryMap SysUser user);
}
