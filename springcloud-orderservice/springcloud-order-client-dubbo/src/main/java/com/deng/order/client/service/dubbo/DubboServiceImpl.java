package com.deng.order.client.service.dubbo;




import com.deng.order.client.service.entity.dubbo.DubboResult;
import com.deng.order.common.entity.User;
import com.deng.order.dao.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DubboServiceImpl implements DubboService{
    private static final Logger LOG = LoggerFactory.getLogger(DubboService.class);
    @Autowired
    private UserMapper userMapper;
    @Override
    public DubboResult<Object> getTest() {
        User user = User.builder().nickname("tom").age("20").build();
        userMapper.insert(user);
        DubboResult result=DubboResult.builder().message("1").body(user).build();
        LOG.info("[服务提供方]执行业务逻辑结束");
        return result;
    }
}

