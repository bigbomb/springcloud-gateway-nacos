package com.deng.order.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "order-service-dev")
public interface Feignclient {
	@RequestMapping(value = "/test", method= RequestMethod.GET)
    String getTest() ;
}
