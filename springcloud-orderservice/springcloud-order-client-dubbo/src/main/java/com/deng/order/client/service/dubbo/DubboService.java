package com.deng.order.client.service.dubbo;

import com.deng.order.client.service.entity.dubbo.Result;

public interface DubboService {
    public Result<Object> getTest();
}