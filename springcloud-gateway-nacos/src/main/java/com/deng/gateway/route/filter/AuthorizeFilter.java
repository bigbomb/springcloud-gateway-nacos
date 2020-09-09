package com.deng.gateway.route.filter;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.deng.gateway.constants.SystemConstants;
import com.deng.gateway.entity.Result;
import com.deng.gateway.strategy.TokenFactory;
import com.deng.gateway.strategy.TokenStrategy;
import reactor.core.publisher.Mono;

/**
 * Token 校验全局过滤器
 */
@Component
@RefreshScope
public class AuthorizeFilter implements GlobalFilter, Ordered {


    @Value("${gateway.notcheckurl}")
    private String notcheckurl;
    
    
    private static final Logger log = LoggerFactory.getLogger( AuthorizeFilter.class );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	String url = exchange.getRequest().getURI().getPath();
    	Result result = null;
    	//判断url是否需要校验
    	if(!isNotCheck(url))
    	{
    		ServerHttpResponse response = exchange.getResponse();
    		 String accesstoken = exchange.getRequest().getHeaders().getFirst(SystemConstants.ACCESS_TOKEN);
    		 String refreshtoken = exchange.getRequest().getHeaders().getFirst(SystemConstants.REFRESH_TOKEN);
    		 String username = exchange.getRequest().getQueryParams().getFirst(SystemConstants.USER_NAME);
    		 //获取refreshtoken策略
    		 TokenStrategy refresstokenStrategy = TokenFactory.getTokenStrategy(SystemConstants.REFRESH_TOKEN);
    		 result = refresstokenStrategy.checkisBlank(refreshtoken,username);
    		 if(result!=null) {
    			 byte[] bits = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
                 DataBuffer buffer = response.bufferFactory().wrap(bits);
                 response.setStatusCode(HttpStatus.UNAUTHORIZED);
                 response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
    			 return response.writeWith(Mono.just(buffer));
    		 }
    		//获取accesstoken策略
    		 TokenStrategy accesstokenStrategy = TokenFactory.getTokenStrategy(SystemConstants.ACCESS_TOKEN);
    		 result = accesstokenStrategy.checkisBlank(accesstoken,username);
    		 if(result != null)
    		 {
    			 byte[] bits = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
                 DataBuffer buffer = response.bufferFactory().wrap(bits);
                 response.setStatusCode(HttpStatus.UNAUTHORIZED);
                 response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
                 return response.writeWith(Mono.just(buffer));
    		 }
    		 
    	}
       
        return chain.filter(exchange);
    }
      
    /**
     * 根据URL判断当前请求是否不需要校验, true:需要校验
     */
    public  boolean isNotCheck(String servletPath) {
        // 若 请求接口 以 / 结尾, 则去掉 /
        servletPath = servletPath.endsWith("/")
                ? servletPath.substring(0,servletPath.lastIndexOf("/"))
                : servletPath;
        System.out.println("servletPath = " + servletPath);
        String[] notcheckurlArr = notcheckurl.split(",");
        for (String path : notcheckurlArr) {
            System.out.println("path = " + path);
            // path 以 /** 结尾, servletPath 以 path 前缀开头
            if (path.endsWith("/**")) {
                String pathStart = path.substring(0, path.lastIndexOf("/")+1);
                System.out.println("pathStart = " + pathStart);
                if (servletPath.startsWith(pathStart)) {
                    return true;
                }
                String pathStart2 = path.substring(0, path.lastIndexOf("/"));
                System.out.println("pathStart2 = " + pathStart2);
                if (servletPath.equals(pathStart2)) {
                    return true;
                }
            }
            // servletPath == path
            if (servletPath.equals(path)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public int getOrder() {
        return 0;
    }
    
}
