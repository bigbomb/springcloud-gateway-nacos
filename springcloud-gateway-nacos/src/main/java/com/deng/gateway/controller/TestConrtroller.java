package com.deng.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.deng.gateway.entity.ErrorResponse;
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
    public String getToken() {
    	String token = null;
    	try {
			token = JwtUtil.createJWT("bigbomb", "bigbomb", "account", 60*1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return token;
    }

    @GetMapping("/fallback")
    public ErrorResponse fallback() {
	    ErrorResponse errorResponse = new ErrorResponse();
	    errorResponse.setCode(500);
	    errorResponse.setMessage("降级处理");
		return errorResponse;
        // return something
    }
}
