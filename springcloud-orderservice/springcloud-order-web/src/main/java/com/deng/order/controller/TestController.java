package com.deng.order.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deng.order.common.entity.TestUser;
import com.deng.order.service.KafkaProviderService;
import com.deng.order.service.RedisService;


@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private KafkaProviderService kafkaProviderService;
    
    @Autowired
    private RedisService redisService;
    
   
    
    @GetMapping("test")
    public String test2(){
        return "getfeignClient";
    }

    @GetMapping("next")
    public String next(){
        return "next1";
    }
    
    @PostMapping("send")
    public String kafkasend() {
    	LocalDateTime localDateTime = LocalDateTime.now();
    	kafkaProviderService.sendMessage(123456, "1231231231232", localDateTime);
		return "ok";
    	
    }
    
  

    @RequestMapping("/user/save")
    public String saveUser() {
        TestUser user = new TestUser();
        user.setName("张三");
        user.setAge("18");
        user.setJob("程序员");
        ArrayList<TestUser> userList = new ArrayList<TestUser>();
        userList.add(user);
        user = new TestUser();
        user.setName("李四");
        user.setAge("28");
        user.setJob("架构师");
        userList.add(user);
        redisService.opsForValueSet(userList);
        return "3333";
    }

    @RequestMapping("/user/get")
    public String getUser() {
    	String getUserString = redisService.opsForValueGet();
        return getUserString.toString();
    }
  

}
