package com.deng.gateway.route;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

@Component
@RefreshScope
public class DynamicRouteServiceImplByNacos {

    private static Logger logger = LoggerFactory.getLogger(DynamicRouteServiceImplByNacos.class);

    @Value("${nacos.dataId}")
    private  String dataId;

    @Value("${nacos.group}")
    private  String group;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private  String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private  String namespace;

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @Bean
    public String routeServiceInit() {
        logger.info("-------------------------------------------------------------------------------");
        logger.info("dataId:{}",dataId);
        logger.info("group:{}",group);
        logger.info("serverAddr:{}",serverAddr);
        dynamicRouteByNacosListener(dataId,group,serverAddr);
        return "success";
    }


    /**
     * 监听Nacos Server下发的动态路由配置
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener (String dataId, String group,String serverAddr){
        try {
            if (group == null || group == ""){
                logger.info("路由group配置为null");
                return;
            }
            if (dataId == null || dataId == ""){
                logger.info("路由dataId配置为null");
                return;
            }
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.NAMESPACE, namespace);
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            ConfigService configService=NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            updateDynamicRoute(content);
            logger.info("nacos初始化监听,{}",content);
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    try {
                    	updateDynamicRoute(configInfo);
                    }catch (Exception e){
                        logger.error("更新配置出错:",e);
                    }
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            logger.error("初始化nacos监听出错:",e);
        }
    }
    public void updateDynamicRoute(String configInfo){
    	List<RouteDefinition> gatewayRouteDefinitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
        for (RouteDefinition routeDefinition : gatewayRouteDefinitions){
            logger.info("遍历:" + routeDefinition.toString());
            dynamicRouteService.update(routeDefinition);
        }
    }
}
