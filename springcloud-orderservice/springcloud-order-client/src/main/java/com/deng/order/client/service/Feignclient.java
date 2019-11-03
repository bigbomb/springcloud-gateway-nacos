package com.deng.order.client.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deng.order.client.service.fallback.FeignClientFallback;
import com.deng.order.common.entity.Result;

@FeignClient(name = "order-service-dev",fallbackFactory = FeignClientFallback.class)
public interface Feignclient {
	@RequestMapping(value = "/test", method= RequestMethod.GET)
    Result<Object> getTest() ;
}
