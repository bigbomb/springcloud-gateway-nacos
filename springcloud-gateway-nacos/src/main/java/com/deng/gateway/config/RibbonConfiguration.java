package com.deng.gateway.config;

import com.deng.gateway.route.rule.NacosWeightRandomRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule(){
        return new NacosWeightRandomRule();

    }
}
