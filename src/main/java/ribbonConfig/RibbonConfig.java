package ribbonConfig;

import com.itmuch.contentcenter.config.NacosWeightedRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/29 23:01
 **/
@Configuration
public class RibbonConfig {
    @Bean
    public IRule ribbonRule() {
//        return new RandomRule();
        return new NacosWeightedRule();
    }
}
