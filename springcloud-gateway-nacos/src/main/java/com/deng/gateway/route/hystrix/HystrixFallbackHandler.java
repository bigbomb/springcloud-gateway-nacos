package com.deng.gateway.route.hystrix;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.deng.gateway.entity.Result;

import reactor.core.publisher.Mono;


@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
	 private static Logger logger = LoggerFactory.getLogger(HystrixFallbackHandler.class);
     private Result RESULT_RESPONSE = new Result();

    @PostConstruct
    private void init() {
    	RESULT_RESPONSE.setCode(500);
    	RESULT_RESPONSE.setMessage("服务异常");
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
            .ifPresent(originalUrls -> logger.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrls));

        return ServerResponse
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(BodyInserters.fromObject(RESULT_RESPONSE));
    }
}