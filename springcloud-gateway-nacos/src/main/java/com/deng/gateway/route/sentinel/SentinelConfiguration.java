package com.deng.gateway.route.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.deng.gateway.entity.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Configuration
public class SentinelConfiguration {
    private Result RESULT_RESPONSE = new Result();
    @PostConstruct
    private void init() {
    	RESULT_RESPONSE.setCode(500);
    	RESULT_RESPONSE.setMessage("您的请求被限流了");
    }
    @Bean
    public BlockRequestHandler blockRequestHandler() {
        return new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                return ServerResponse.status(500)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(RESULT_RESPONSE));
            }
        };
    }
}
