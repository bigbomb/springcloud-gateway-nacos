package com.deng.order.client.service.dubbo;

import com.deng.order.client.service.entity.dubbo.DubboResult;

public interface DubboService {
    public DubboResult<Object> getTest();
}