package com.deng.order.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Oct 30, 2019 6:14:05 PM 

* 类说明 

*/
@Service
public class RedisService {
	  @Autowired
	  private RedisTemplate<String,Object> redisTemplate;
	  public <T> void opsForValueSet(ArrayList<T> userList)
	  {
	        redisTemplate.opsForValue().set("userZhang", JSONObject.toJSONString(userList));
	  }
	  
	  public String opsForValueGet()
	  {
		String getUserString = (String) redisTemplate.opsForValue().get("userZhang");
		return getUserString;
		  
	  }
}
