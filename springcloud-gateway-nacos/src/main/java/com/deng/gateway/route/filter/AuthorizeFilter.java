package com.deng.gateway.route.filter;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
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
import com.alibaba.fastjson.JSONObject;
import com.deng.gateway.entity.StatusCodeConstants;
import com.deng.gateway.utils.JwtUtil;
import io.jsonwebtoken.Claims;
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

    private static final String AUTHORIZE_TOKEN = "token";

    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	String url = exchange.getRequest().getURI().getPath();
    	//判断url是否需要校验
    	if(!isNotCheck(url))
    	{
    		ServerHttpResponse response = exchange.getResponse();
    		 String token = exchange.getRequest().getQueryParams().getFirst( AUTHORIZE_TOKEN );
    		   //判断token是否为空
    	        if ( StringUtils.isBlank( token )) {
    	            log.info( "token is empty ..." );
    	            JSONObject message = new JSONObject();
                    message.put("code", StatusCodeConstants.TOKEN_NONE);
                    message.put("message", "鉴权失败，无token");
                    byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
                    return response.writeWith(Mono.just(buffer));
    	        }
    	        else {
    	        	 Claims claims = JwtUtil.getClaimByToken(token);
    	             System.out.println("TOKEN: " + claims);
    	             // 判断签名是否存在或过期
    	             boolean b = claims==null || claims.isEmpty() || JwtUtil.isTokenExpired(claims.getExpiration());
    	             if (b) {
    	            	 log.info( "token is Expired ..." );
    	            	 JSONObject message = new JSONObject();
	                     message.put("code", StatusCodeConstants.TOKEN_NONE);
	                     message.put("message", "鉴权失败，token失效");
	                     byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
	                     DataBuffer buffer = response.bufferFactory().wrap(bits);
	                     response.setStatusCode(HttpStatus.UNAUTHORIZED);
	                     response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
	                     return response.writeWith(Mono.just(buffer));
    	             }
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
