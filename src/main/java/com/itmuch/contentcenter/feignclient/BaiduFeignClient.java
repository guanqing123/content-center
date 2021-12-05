package com.itmuch.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/5 21:11
 **/
/** 脱离ribbon的使用 */
@FeignClient(name = "baidu", url = "http://www.baidu.com")
public interface BaiduFeignClient {
    @GetMapping("")
    String index();
}
