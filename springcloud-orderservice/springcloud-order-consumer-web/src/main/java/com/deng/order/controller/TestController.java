package com.deng.order.controller;

import com.deng.order.client.service.dubbo.DubboService;
import com.deng.order.client.service.entity.dubbo.Result;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.deng.order.client.service.Feignclient;



@RestController
@RequestMapping("/")
public class TestController {

    
//    @Autowired
//    private Feignclient feignclient;

    @Reference(check=false)
    private DubboService dubboService;

    /**
     * 测试feignclient的功能
     * @return
     */
//    @GetMapping("feignget")
//    public Result<Object> feignget(){
//    	Result<Object> result = feignclient.getTest();
//        return result;
//    }

    //    @Autowired
//    private RestTemplate restTemplate;


    @GetMapping("dubbotest")
    public Result<Object> dubbotest() {

        return dubboService.getTest();
    }




}
