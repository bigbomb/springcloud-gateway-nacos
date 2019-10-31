package com.deng.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, RedisReactiveAutoConfiguration.class})
public class OrderServiceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceServerApplication.class, args);
    }
}
