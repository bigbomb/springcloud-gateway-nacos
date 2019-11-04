package com.deng.gateway.strategy;

import org.springframework.http.server.reactive.ServerHttpResponse;

import com.deng.gateway.entity.Result;

import reactor.core.publisher.Mono;

public interface TokenStrategy {
	public Result checkisBlank(String token, ServerHttpResponse response);

}
