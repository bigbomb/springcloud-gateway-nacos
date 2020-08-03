package com.deng.order.client.service.dubbo;



import com.deng.order.client.service.entity.dubbo.Result;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DubboServiceImpl implements DubboService{


    @Override
    public Result<Object> getTest() {
       Result result=Result.builder().message("1").build();
       return result;
    }
}

