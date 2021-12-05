package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.modular.dev.model.HyFans;
import com.itmuch.contentcenter.modular.dev.service.HyFansService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/28 16:49
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FansController {

    @Autowired
    private HyFansService hyFansService;

    private final RestTemplate restTemplate;

    private final UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private DiscoveryClient discoveryClient;

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

    @GetMapping("/getFan2")
    public HyFans getFan2(){
        HyFans hyFans = hyFansService.getFan();

        // 强调:
        // 了解stream --> JDK8
        // lambda表达式
        // functional --> 函数式编程
        //用户中心所有实例的信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        String targetURL = instances.stream()
                // 数据变换
                .map(instance -> instance.getUri().toString() + "/users/{id}")
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前没有实例!"));

        log.info("请求的目标地址：{}", targetURL);

        String userStr = this.restTemplate.getForObject(
                targetURL,
                String.class,
                hyFans.getSex());
        hyFans.setNickName(userStr);
        return hyFans;
    }

    /**
     * 手写一个客户端侧负载均衡器
     * @Author guanqing
     * @Date 2021/11/29 22:26
     **/
    @GetMapping("/getFan3")
    public HyFans getFan3(){
        HyFans hyFans = hyFansService.getFan();

        // 强调:
        // 了解stream --> JDK8
        // lambda表达式
        // functional --> 函数式编程
        //用户中心所有实例的信息
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        List<String> targetURLS = instances.stream()
                // 数据变换
                .map(instance -> instance.getUri().toString() + "/users/{id}")
                .collect(Collectors.toList());

        int i = ThreadLocalRandom.current().nextInt(targetURLS.size());

        String targetURL = targetURLS.get(i);
        log.info("请求的目标地址：{}", targetURL);
        String userStr = this.restTemplate.getForObject(
                targetURL,
                String.class,
                hyFans.getSex());
        hyFans.setNickName(userStr);
        return hyFans;
    }

    /**
     * 使用 ribbon
     * @Author guanqing
     * @Date 2021/11/29 22:30
     **/
    @GetMapping("/getFan4")
    public HyFans getFan4(){
        HyFans hyFans = hyFansService.getFan();

        /** 当restTemplate发起请求的时候,ribbon会自动把 user-center
          替换成用户中心在 nacos 上的地址,并且进行负载均衡算法,
          计算出一个实例给我们请求 */
        String userStr = this.restTemplate.getForObject(
                "http://user-center/users/{id}",
                String.class,
                hyFans.getSex());
        hyFans.setNickName(userStr);
        return hyFans;
    }

    /**
     * 使用 feign
     * @Author guanqing
     * @Date 2021/11/29 22:30
     **/
    @GetMapping("/getFan5")
    public HyFans getFan5(){
        HyFans hyFans = hyFansService.getFan();

        /** 当restTemplate发起请求的时候,ribbon会自动把 user-center
          替换成用户中心在 nacos 上的地址,并且进行负载均衡算法,
          计算出一个实例给我们请求 */
        String userStr = this.userCenterFeignClient.findUserById(hyFans.getSex());

        hyFans.setNickName(userStr);
        return hyFans;
    }
}
