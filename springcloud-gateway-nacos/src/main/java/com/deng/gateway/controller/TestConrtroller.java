package com.deng.gateway.controller;


import com.deng.gateway.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.entity.Tokens;
import com.deng.gateway.utils.JwtUtil;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestConrtroller {



    @GetMapping("getToken")
    public Result getToken() {
    	String accessToken = null;
    	String refreshToken = null;
    	Result<?> result = null;
    	Tokens tokens = null;
    	try {
			accessToken = JwtUtil.createJWT("bigbomb", "bigbomb", "account", 60*1000);
			refreshToken = JwtUtil.createJWT("system", "system", "refreshToken", 1296000*1000);
		    tokens = Tokens.builder().accessToken(accessToken).refreshToken(refreshToken).build();
			result = Result.builder().code(StatusCodeConstants.STATUS_SUCCESS).body(tokens).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
//
//    @GetMapping("fallback")
//    public Result fallback() {
//	    Result errorResponse = new Result();
//	    errorResponse.setCode(500);
//	    errorResponse.setMessage("降级处理");
//		return errorResponse;
//        // return something
//    }
}
