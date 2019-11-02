package com.deng.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.deng.gateway.constants.StatusCodeConstants;
import com.deng.gateway.entity.Result;
import com.deng.gateway.entity.Tokens;
import com.deng.gateway.utils.JwtUtil;

@RestController
public class TestConrtroller {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("test")
    public String test() {
        return restTemplate.getForObject("http://order-service-dev/test", String.class);
    }
    
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
//    @GetMapping("fallback1")
//    public ErrorResponse fallback() {
//	    ErrorResponse errorResponse = new ErrorResponse();
//	    errorResponse.setCode(500);
//	    errorResponse.setMessage("降级处理");
//		return errorResponse;
//        // return something
//    }
}
