package com.deng.order.client.service.dubbo;




import com.deng.order.client.service.entity.dubbo.DubboResult;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DubboServiceImpl implements DubboService{


    @Override
    public DubboResult<Object> getTest() {
       DubboResult result=DubboResult.builder().message("1").build();
       return result;
    }
}

