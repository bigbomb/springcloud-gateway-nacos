package com.deng.order.client.service;


import com.deng.order.client.service.entity.feign.FeignResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deng.order.client.service.fallback.FeignClientFallback;

@FeignClient(name = "order-service-provider",fallbackFactory = FeignClientFallback.class)
public interface Feignclient {
	@RequestMapping(value = "/sys-user/list", method= RequestMethod.GET)
    FeignResult<Object> getTest() ;
}
