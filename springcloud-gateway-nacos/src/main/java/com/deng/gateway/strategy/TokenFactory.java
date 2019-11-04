package com.deng.gateway.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpResponse;

import com.deng.gateway.entity.Result;


public class TokenFactory {
	private static Map<String,TokenStrategy> tokenMap = new HashMap<String,TokenStrategy>();
	 
    private TokenFactory() {}
 
    private static final TokenStrategy EMPTY = new EmptyToken();
 
    //获取tokenStrategy对象
    public static TokenStrategy getTokenStrategy(String state) {
    	TokenStrategy result = tokenMap.get(state);
        return result == null ? EMPTY : result;
    }
 
    //将tokenStrategy对象注册到这里
    public static void registerToken(String state,TokenStrategy o){
    	tokenMap.put(state, o);
    }
 
    private static class EmptyToken implements TokenStrategy {
        
		@Override
		public Result checkisBlank(String token,ServerHttpResponse response) {
			// TODO Auto-generated method stub
			return null;
		}
    }
}
