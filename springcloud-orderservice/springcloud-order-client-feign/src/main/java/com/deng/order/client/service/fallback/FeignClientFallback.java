package com.deng.order.client.service.fallback;

import com.deng.order.client.service.constant.SystemConstant;
import com.deng.order.client.service.entity.feign.FeignResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.deng.order.client.service.Feignclient;


import feign.hystrix.FallbackFactory;
import org.springframework.util.StringUtils;

@Component
public class FeignClientFallback implements FallbackFactory<Feignclient> {
    private static final Logger LOG = LoggerFactory.getLogger(FeignClientFallback.class);

    @Override
    public Feignclient create(Throwable throwable) {
        String msg = throwable == null ? "" : throwable.getMessage();
        if (!StringUtils.isEmpty(msg)) {
            LOG.error(msg);
        }
        return new Feignclient() {
            @Override
            public FeignResult<Object> getTest(){
                LOG.info("您的请求被降级了！");
                throw new RuntimeException();
//                return FeignResult.builder().code(SystemConstant.RESULT_CODE_FAILURE).message(SystemConstant.RESULT_SERVICE_FAILURE).build();
            }
        };
    }
}