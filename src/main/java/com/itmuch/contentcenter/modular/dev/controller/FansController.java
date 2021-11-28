package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.modular.dev.model.HyFans;
import com.itmuch.contentcenter.modular.dev.service.HyFansService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/28 16:49
 **/
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FansController {

    @Autowired
    private HyFansService hyFansService;

    private final RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/getFans")
    public List<HyFans> getFans(){
        List<HyFans> hyFans = hyFansService.getFans();
        return hyFans;
    }

    @GetMapping("/getFan")
    public HyFans getFan(){
        HyFans hyFans = this.hyFansService.getFan();
        String userStr = this.restTemplate.getForObject(
                "http://localhost:8080/users/{id}",
                String.class,
                hyFans.getSex());
        hyFans.setNickName(userStr);
        return hyFans;
    }

    /**
     * 测试：服务发现,证明内容中心总能找到用户中心 返回用户中心所有实例的地址信息
     * @Author guanqing
     * @Date 2021/11/29 0:20
     **/
    @GetMapping("/getInstances")
    public List<ServiceInstance> getInstances(){
        // 查询指定服务的所有实例的信息
        return this.discoveryClient.getInstances("user-center");
    }
}
