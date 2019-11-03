package com.deng.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deng.order.client.service.Feignclient;
import com.deng.order.common.entity.Result;


@RestController
@RequestMapping("/")
public class TestController {

    
    @Autowired
    private Feignclient feignclient;

    /**
     * 测试feignclient的功能
     * @return
     */
    @GetMapping("feignget")
    public Result<Object> feignget(){
    	Result<Object> result = feignclient.getTest();
        return result;
    }
   
  

}
