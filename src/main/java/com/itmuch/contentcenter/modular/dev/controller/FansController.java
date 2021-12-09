package com.itmuch.contentcenter.modular.dev.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.itmuch.contentcenter.auth.CheckLogin;
import com.itmuch.contentcenter.feignclient.ArgsUserCenterFeignClient;
import com.itmuch.contentcenter.feignclient.BaiduFeignClient;
import com.itmuch.contentcenter.feignclient.UserCenterFeignClient;
import com.itmuch.contentcenter.modular.dev.model.HyFans;
import com.itmuch.contentcenter.modular.dev.model.param.SysUser;
import com.itmuch.contentcenter.modular.dev.service.HyCommonService;
import com.itmuch.contentcenter.modular.dev.service.HyFansService;
import com.itmuch.contentcenter.sentinel.BlockHandlerClass;
import com.itmuch.contentcenter.sentinel.FallbackClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
    @CheckLogin
    public HyFans getFan5(){
        HyFans hyFans = hyFansService.getFan();

        /** 当restTemplate发起请求的时候,ribbon会自动把 user-center
          替换成用户中心在 nacos 上的地址,并且进行负载均衡算法,
          计算出一个实例给我们请求 */
        String userStr = this.userCenterFeignClient.findUserById(hyFans.getSex());

        hyFans.setNickName(userStr);
        return hyFans;
    }

    /**
     * RESTfulURL 支持
     * @Author guanqing
     * @Date 2021/12/7 15:56
     **/
    @GetMapping("/getRestFul/{id}")
    public String getRestFul(@PathVariable String id){
        return this.userCenterFeignClient.findUserById(id);
    }

    private final ArgsUserCenterFeignClient argsUserCenterFeignClient;

    /**
     * 使用 feign 多参数
     * @Author guanqing
     * @Date 2021/12/5 20:35
     **/
    @GetMapping("/getFan6")
    public HyFans getFan6(SysUser user){
        HyFans hyFans = hyFansService.getFan();

        String userStr = this.argsUserCenterFeignClient.query(user);

        hyFans.setNickName(userStr);
        return hyFans;
    }

    private final BaiduFeignClient baiduFeignClient;

    @GetMapping("/baidu")
    public String baidu(){
        return this.baiduFeignClient.index();
    }

    private final HyCommonService hyCommonService;

    @GetMapping("test-a")
    public String testA(){
        this.hyCommonService.common();
        return "test-a";
    }

    @GetMapping("test-b")
    public String testB(){
        this.hyCommonService.common();
        return "test-b";
    }

    /**
     * 热点规则测试
     * @Author guanqing
     * @Date 2021/12/6 21:02
     **/
    @GetMapping("test-hot")
    @SentinelResource("hot")
    public String hot(
        @RequestParam(required = false) String a,
        @RequestParam(required = false) String b
    ){
        return a + " " + b;
    }

    /**
     * 代码生成流量控制规则
     * @Author guanqing
     * @Date 2021/12/6 21:16
     **/
    @GetMapping("test-add-flow-rule")
    public String codeFlowRule(){
        this.initFlowQpsRule();
        return "success";
    }

    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/getFan4");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    /**
     * Sentinel API
     * @Author guanqing
     * @Date 2021/12/6 22:38
     **/
    @GetMapping("test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a){

        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName, "test-wfw");

        Entry entry = null;
        try {
            /** 定义一个sentinel保护的资源,资源名称是test-sentinel-api */
            entry = SphU.entry(resourceName);
            /** 被保护的业务逻辑 */
            if (StringUtils.isBlank(a)) {
                throw new  IllegalArgumentException("a不能为空");
            }
            return a;
        }
        /** 如果被保护的资源被限流或者降级了,就会抛BlockException */
        catch (BlockException e) {
            log.warn("限流,或者降级了", e);
            return "限流,或者降级了";
        } catch (IllegalArgumentException e) {
            /** 统计IllegalArgumentException 【发生的次数、发生占比...】 */
            Tracer.trace(e);
            return "参数非法";
        }
        finally {
            if (entry != null) {
                /** 退出entry */
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    /**
     * 使用 @SentinelResource（不支持'针对来源'） 重构
     * @Author guanqing
     * @Date 2021/12/6 22:38
     **/
    @GetMapping("test-sentinel-resource")
    @SentinelResource(
        value = "test-sentinel-api",
        blockHandler = "block",
        blockHandlerClass = BlockHandlerClass.class,
        fallback = "fallback",
        fallbackClass = FallbackClass.class
    )
    public String testSentinelResource(@RequestParam(required = false) String a){
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a不能为空");
        }
        return a;
    }

    /**
     * restTemplate 整合 sentinel
     * @Author guanqing
     * @Date 2021/12/6 23:21
     **/
    @GetMapping("test-rest-template-sentinel/{id}")
    public String sentinelRestTemplate(@PathVariable Integer id){
        return this.restTemplate.getForObject("http://user-center/users/{id}", String.class, id);
    }
}
