package com.deng.order.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }
}
