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
public class RefreshTokenStrategy implements TokenStrategy,InitializingBean{
	 private static final Logger log = LoggerFactory.getLogger( RefreshTokenStrategy.class );
	@Autowired
	private JWTDefinition jwtDefinition ;
	 @Override
		public Result checkisBlank(String token, String username) {
			Result result = null;
			if(StringUtils.isBlank(token))
			{
				// TODO Auto-generated method stub
				 log.info( "refreshToken is empty ..." );
		         result = Result.builder()
				    		.message("refreshToken不能为空")
				    		.code(StatusCodeConstants.REFRESH_TOKEN_EMPTY)
				    		.body(null)
				    		.build(); 
			}
			 else {
				Claims refreshclaims = null;
				try {
					refreshclaims = jwtDefinition.getClaimByToken(token);
					String accessToken = null;
					accessToken = jwtDefinition.generateAccessToken(username);
					Tokens newtoken = Tokens.builder().accessToken(accessToken).build();
					result = Result.builder()
							.message("accesstoken重新生成")
							.code(StatusCodeConstants.ACCESS_TOKEN_EXPIRE)
							.body(newtoken)
							.build();
				} catch (SignatureException e) {
					// TODO Auto-generated catch block
					log.info("refreshtoken格式有误 ...");
					result = Result.builder()
							.message("refreshtoken格式有误 ")
							.code(StatusCodeConstants.UNFORMAT_ACCESS_TOKEN)
							.body(null)
							.build();
					e.printStackTrace();
				}catch (ExpiredJwtException e) {
					// TODO Auto-generated catch block
					log.info("refreshtoken过期了");
					String refreshToken = null;
					String accessToken = null;
					refreshToken = jwtDefinition.generateRefreshToken(username);
					accessToken = jwtDefinition.generateAccessToken(username);
					Tokens newtoken = Tokens.builder().refreshToken(refreshToken).accessToken(accessToken).build();
					result = Result.builder()
							.message("refreshtoken,accesstoken重新生成")
							.code(StatusCodeConstants.REFRESH_TOKEN_EXPIRE)
							.body(newtoken)
							.build();
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
