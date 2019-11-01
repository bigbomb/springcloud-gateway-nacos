package com.deng.order.client.service.fallback;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.deng.order.client.service.Feignclient;
import com.deng.order.common.constant.SystemConstant;
import com.deng.order.common.entity.Result;

import feign.hystrix.FallbackFactory;

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
            public Result getTest(){
                return Result.builder().code(SystemConstant.RESULT_CODE_FAILURE).message(SystemConstant.RESULT_SERVICE_FAILURE).build();
            }
        };
    }
}