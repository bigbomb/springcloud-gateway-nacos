package com.deng.order.controller;

import com.deng.order.client.service.Feignclient;
import com.deng.order.client.service.dubbo.DubboService;
import com.deng.order.client.service.entity.dubbo.DubboResult;
import com.deng.order.client.service.entity.feign.FeignResult;

import com.deng.order.common.entity.UserInfoIDto;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//import com.deng.order.client.service.Feignclient;



@RestController
@RequestMapping("/")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
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
    public FeignResult<Object> feignget(@RequestBody @Valid UserInfoIDto userInfoIDto) {
        FeignResult<Object> result = new FeignResult<Object>();
        try{
            result = feignclient.getTest();
            dubboService.getTest();
            LOG.info("[服务消费方]执行业务逻辑开始");
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
