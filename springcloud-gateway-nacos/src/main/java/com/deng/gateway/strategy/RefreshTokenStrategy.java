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
public class RefreshTokenStrategy implements TokenStrategy,InitializingBean{
	 private static final Logger log = LoggerFactory.getLogger( RefreshTokenStrategy.class );
	 @Override
		public Result checkisBlank(String token, ServerHttpResponse response) {
			Result result = null;
			Tokens newtoken ;
			if(StringUtils.isBlank(token))
			{
				// TODO Auto-generated method stub
				 log.info( "refreshToken is empty ..." );
		         result = Result.builder()
				    		.message("refreshToken不能为空")
				    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
				    		.body(null)
				    		.build(); 
			}
			 else {
	        	 Claims accessclaims = JwtUtil.getClaimByToken(token);
	        	 boolean b = accessclaims==null || accessclaims.isEmpty() || JwtUtil.isTokenExpired(accessclaims.getExpiration());
	        	 if (b)
	        	 {
	        		 log.info( "refreshtoken is Expired ..." );
         		    String refreshToken = null;
						try {
							refreshToken = JwtUtil.createJWT("system", "system", "refreshToken", 1296000*1000);
							newtoken = Tokens.builder().refreshToken(refreshToken).build();
						    result = Result.builder()
						    		.message("旧的refreshtoken失效，重新生成新的refreshtoken")
						    		.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
						    		.body(newtoken)
						    		.build();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        	 }
			 }
			return result;

			 
		}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		TokenFactory.registerToken(SystemConstants.REFRESH_TOKEN, this);
	}


}
