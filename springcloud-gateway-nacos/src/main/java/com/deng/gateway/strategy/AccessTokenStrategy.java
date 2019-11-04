package com.deng.gateway.strategy;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.constants.SystemConstants;
import com.deng.gateway.entity.Result;
import com.deng.gateway.entity.Tokens;
import com.deng.gateway.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class AccessTokenStrategy implements TokenStrategy,InitializingBean{
	 private static final Logger log = LoggerFactory.getLogger( AccessTokenStrategy.class );
	@Override
	public Result checkisBlank(String token, ServerHttpResponse response) {
		Result result = null;
		Tokens newtoken ;
		if(StringUtils.isBlank(token))
		{
			// TODO Auto-generated method stub
			 log.info( "accessToken is empty ..." );
	         result = Result.builder()
			    		.message("accessToken不能为空")
			    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
			    		.body(null)
			    		.build(); 
		}
		 else {
        	 Claims accessclaims = JwtUtil.getClaimByToken(token);
        	 boolean b = accessclaims==null || accessclaims.isEmpty() || JwtUtil.isTokenExpired(accessclaims.getExpiration());
        	 if(b)
        	 {
        		 try {
         			log.info( "accesstoken is Expired ..." );
						String accessToken = JwtUtil.createJWT("bigbomb", "bigbomb", "account", 60*1000);
					    newtoken = Tokens.builder()
					    		.accessToken(accessToken)
					    		.build();
						result = Result.builder()
								.message("旧的accesstoken失效，重新生成新的accesstoken")
								.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
								.body(newtoken)
								.build();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

        	 }else {
        		 if(SystemConstants.REFRESH_TOKEN.equals(accessclaims.getSubject()))
    			 {
        		 log.info( "accesstoken is not refreshtoken ..." );
    	            result = Result.builder()
				    		.message("accesstoken不能是refreshtoken")
				    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
				    		.body(null)
				    		.build();
    			 }
        	 }
        	
		 }
		return result;

		 
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		TokenFactory.registerToken(SystemConstants.ACCESS_TOKEN, this);
	}


}
