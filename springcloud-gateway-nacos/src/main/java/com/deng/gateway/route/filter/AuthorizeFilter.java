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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.entity.Result;
import com.deng.gateway.entity.Tokens;
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

    private static final String ACCESS_TOKEN = "accesstoken";
    private static final String REFRESH_TOKEN = "refreshtoken";

    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	String url = exchange.getRequest().getURI().getPath();
    	//判断url是否需要校验
    	if(!isNotCheck(url))
    	{
    		ServerHttpResponse response = exchange.getResponse();
    		 String accesstoken = exchange.getRequest().getQueryParams().getFirst( ACCESS_TOKEN );
    		 String refreshtoken = exchange.getRequest().getQueryParams().getFirst( REFRESH_TOKEN );
    		 Result result = null;
    		 Tokens tokens = null;
    		   //判断token是否为空
    	        if ( StringUtils.isBlank( accesstoken ) || StringUtils.isBlank( refreshtoken )) {
    	            log.info( "accesstoken or refreshtoken is empty ..." );
    	            result = Result.builder()
				    		.message("accesstoken或者refreshtoken不能为空")
				    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
				    		.body(tokens)
				    		.build();
                    byte[] bits = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
                    return response.writeWith(Mono.just(buffer));
    	        }
    	        else {
    	        	 Claims accessclaims = JwtUtil.getClaimByToken(accesstoken);
    	        	 Claims refreshclaims = JwtUtil.getClaimByToken(refreshtoken);
    	             System.out.println("TOKEN: " + accessclaims);

    	             // 判断签名是否存在或过期
    	             boolean b = accessclaims==null || accessclaims.isEmpty() || JwtUtil.isTokenExpired(accessclaims.getExpiration());
    	             if (b) {
    	            	 boolean c = refreshclaims==null || refreshclaims.isEmpty() || JwtUtil.isTokenExpired(refreshclaims.getExpiration());
    	            	 if(!c)
    	            	 {
    	            		 try {
    	            			log.info( "accesstoken is Expired ..." );
								String accessToken = JwtUtil.createJWT("bigbomb", "bigbomb", "account", 60*1000);
							    tokens = Tokens.builder()
							    		.accessToken(accessToken)
							    		.build();
								result = Result.builder()
										.message("旧的accesstoken失效，重新生成新的accesstoken")
										.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
										.body(tokens)
										.build();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
    	            	 }
    	            	 else {
    	            		    log.info( "refreshtoken is Expired ..." );
    	            		    String refreshToken = null;
								try {
									refreshToken = JwtUtil.createJWT("system", "system", "refreshToken", 1296000*1000);
									tokens = Tokens.builder().refreshToken(refreshToken).build();
								    result = Result.builder()
								    		.message("旧的refreshtoken失效，重新生成新的refreshtoken")
								    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
								    		.body(tokens)
								    		.build();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							  
						}
	                     byte[] bits = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
	                     DataBuffer buffer = response.bufferFactory().wrap(bits);
//	                     response.setStatusCode(HttpStatus.UNAUTHORIZED);
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
