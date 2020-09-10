package com.deng.order.controller;

import com.deng.order.client.service.Feignclient;
import com.deng.order.client.service.dubbo.DubboService;
import com.deng.order.client.service.entity.dubbo.DubboResult;
import com.deng.order.client.service.entity.feign.FeignResult;
import com.deng.order.common.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//import com.deng.order.client.service.Feignclient;



@RestController
@RequestMapping("/")
public class TestController {

    
    @Autowired
    private Feignclient feignclient;

    @Reference(check=false,retries=0)
    private DubboService dubboService;

    /**
     * 测试feignclient的功能
     * @return
     */
    @GetMapping("feignget")
    @GlobalTransactional(name = "order-service-consumer")
    public FeignResult<Object> feignget() {
        FeignResult<Object> result = new FeignResult<Object>();
        try{
            result = feignclient.getTest();
            dubboService.getTest();
        }catch(Exception e)
        {
            throw new RuntimeException();
        }

        return result;
    }

    //    @Autowired
//    private RestTemplate restTemplate;


@GetMapping("dubbotest")
    public DubboResult<Object> dubbotest() {

        return dubboService.getTest();
    }




}
