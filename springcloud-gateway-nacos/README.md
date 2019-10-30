# springcloud-gateway-nacos

#### 介绍
springcloud gateway+nacos 实现动态路由 demo,参考了网上一些文章 博客,然后调试完成

#### 软件架构
软件架构说明
首先参考nacos官方文档,安装nacos的server服务

#### nacos 配置截图

![输入图片说明](https://gitee.com/uploads/images/2019/0430/112048_83a185da_1351968.jpeg "1556594403(1).jpg")

#### 一. gateway-server.yml配置
```
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${spring.cloud.nacos.config.server-addr}
      locator:
          enabled: true
          lowerCaseServiceId: true

        
nacos:
  dataId: gateway_config_json
  group: refresh_config_json

server:
  port: 5008

# 配置输出日志
logging:
  level:
    org.springframework.cloud.gateway: TRACE
    org.springframework.http.server.reactive: DEBUG
    org.springframework.web.reactive: DEBUG
    reactor.ipc.netty: DEBUG

#开启端点
management:
  endpoints:
    web:
      exposure:
        include: '*'
  security:
    enabled: false

```
#### 二. gateway_config_json, 这是动态路由规则配置,注意，这里是json格式的,详细可看项目中的代码

```
[{
    "filters": [{
         "args": {
            "parts": "1"
        },
        "name": "StripPrefix"
    }],
    "id": "hello-service",
    "order": 0,
    "predicates": [{
        "args": {
            "pattern": "/hello-service/**"
        },
        "name": "Path"
    }],
    "uri": "lb://hello-service"
},{
    "filters": [{
         "args": {
            "parts": "1"
        },
        "name": "StripPrefix"
    }],
    "id": "eat-service",
    "order": 0,
    "predicates": [{
        "args": {
            "pattern": "/eat-service/**"
        },
        "name": "Path"
    }],
    "uri": "lb://eat-service"
}]

```
#### 三.启动测试
```
 这里启动之后，项目中会有对nacos配置的监听,每次更新路由配置后,会动态的刷新到项目中
```


#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. xxxx
2. xxxx
3. xxxx

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 码云特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)