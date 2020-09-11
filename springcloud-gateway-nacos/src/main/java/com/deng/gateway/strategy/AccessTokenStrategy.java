package com.deng.gateway.strategy;


import com.deng.gateway.entity.JWTDefinition;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
public class AccessTokenStrategy implements TokenStrategy,InitializingBean{
	 private static final Logger log = LoggerFactory.getLogger( AccessTokenStrategy.class );
	 @Autowired
	 private JWTDefinition jwtDefinition ;
	@Override
	public Result checkisBlank(String token, String username) {
		Result result = null;
		if(StringUtils.isBlank(token))
		{
			// TODO Auto-generated method stub
			 log.info( "accessToken is empty ..." );
	         result = Result.builder()
			    		.message("accessToken不能为空")
			    		.code(StatusCodeConstants.ACCESS_TOKEN_EMPTY)
			    		.body(null)
			    		.build(); 
		}
		 else {
				Claims accessclaims = null;
				try {
					accessclaims = jwtDefinition.getClaimByToken(token);
                    if (SystemConstants.REFRESH_TOKEN.equals(accessclaims.getSubject())) {
						log.info("accesstoken不能用refreshtoken ...");
						result = Result.builder()
								.message("")
								.code(StatusCodeConstants.TOKEN_WRONG)
								.body(null)
								.build();

					}
				} catch (SignatureException e) {

					// TODO Auto-generated catch block
					log.info("accesstoken格式有误 ...");
					result = Result.builder()
							.message("accesstoken格式有误...")
							.code(StatusCodeConstants.UNFORMAT_ACCESS_TOKEN)
							.body(null)
							.build();
					e.printStackTrace();
				}catch (ExpiredJwtException e) {
					// TODO Auto-generated catch block
					log.info("旧的accesstoken过期了，重新生成新的accesstoken");
//					String accessToken = jwtDefinition.generateAccessToken(username);
//					Tokens newtoken = Tokens.builder()
//							.accessToken(accessToken)
//							.build();
					result = Result.builder()
							.message("accesstoken过期了")
							.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
							.body(null)
							.build();
					e.printStackTrace();
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
