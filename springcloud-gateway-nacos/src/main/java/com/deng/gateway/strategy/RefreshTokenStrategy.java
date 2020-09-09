package com.deng.gateway.strategy;


import com.deng.gateway.entity.JWTDefinition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.constants.SystemConstants;
import com.deng.gateway.entity.Result;
import com.deng.gateway.entity.Tokens;
import io.jsonwebtoken.Claims;

@Component
public class RefreshTokenStrategy implements TokenStrategy,InitializingBean{
	 private static final Logger log = LoggerFactory.getLogger( RefreshTokenStrategy.class );
	@Autowired
	private JWTDefinition jwtDefinition ;
	 @Override
		public Result checkisBlank(String token, String username) {
			Result result = null;
			Tokens newtoken;
			if(StringUtils.isBlank(token))
			{
				// TODO Auto-generated method stub
				 log.info( "refreshToken is empty ..." );
		         result = Result.builder()
				    		.message("refreshToken不能为空")
				    		.code(StatusCodeConstants.REFRESH_TOKEN_EXPIRE)
				    		.body(null)
				    		.build(); 
			}
			 else {
				Claims accessclaims = jwtDefinition.getClaimByToken(token);
	        	 boolean b = accessclaims==null || accessclaims.isEmpty() || jwtDefinition.isTokenExpired(accessclaims.getExpiration());
	        	 if (b)
	        	 {
	        	 	log.info( "accesstoken,refreshtoken is reset ..." );
         		    String refreshToken = null;
         		    String accessToken = null;
						try {
							refreshToken = jwtDefinition.generateRefreshToken(username);
							accessToken = jwtDefinition.generateAccessToken(username);
							newtoken = Tokens.builder().refreshToken(refreshToken).accessToken(accessToken).build();
						    result = Result.builder()
						    		.message("refreshtoken,accesstoken重新生成")
						    		.code(StatusCodeConstants.REFRESH_TOKEN_EXPIRE)
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
