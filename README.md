快速开始
1、先觉条件

 # git
 # java8
 # mysql 5.6
 # redis 5.0+
 # elasticsearch 7.2.0
 # kafka_2.11-2.3.0
 # nacos 1.1.4
注:项目所有配置参数全部由nacos管理，保证了项目参数的保密性，如有需要动态变更的，可以在项目中采用@RefreshScope来达到实时刷新

2、项目结构

 #1、主项目 pom 文件
 #2、springcloud-gateway-nacos 网关服务 
 ├── com.deng.gateway
 ├──controller --控制类
 ├──entity --实体类
 ├──route --网关路由的配置
 ├──uitls --网关项目工具包
 └── pom.xml --子项目 maven 配置文件
 #3、order 测试服务
 ├──springcloud-order-biz-service ---服务子项目
 ├──springcloud-order-client ---接口子项目，比如feignclient 接口对外暴露
 ├──springcloud-order-common ---公共子项目
 ├──springcloud-order-dao ---持久层子项目
 ├──springcloud-order-integration ---集成配置子项目
 ├──springcloud-order-web ---控制层子项目
 ├──springcloud-order-start ---开启入口子项目
 └──order服务pom文件
 #4、nacos_config.zip nacos配置文件
3、目前集成的功能

 #1、mybatis
 #2、jwt token
 #3、redis
 #4、elasticsearch
 #5、kafka
 #6、nacos gateway动态路由，实时刷新
 #7、gateway url白名单实时刷新
 #8、gateway hystrix ribbon负载均衡的开启，可以试验同时开启项目中的两个start
3、后续增加

#1、feignclient
#2、logback
#3、分布式事务
