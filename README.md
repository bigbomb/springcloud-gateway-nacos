<!DOCTYPE html> <html lang="zh"> <head> <meta charset="utf-8"/><link rel="shortcut icon" href="https://www.mdeditor.com/images/logos/favicon.ico" type="image/x-icon"/> </head> <body><h1 id="h1-u5FEBu901Fu5F00u59CB"><a name="快速开始" class="reference-link"></a><span class="header-link octicon octicon-link"></span>快速开始，本项目是一个微服务脚手架工程，为了方便大家可以快速入手开发功能而建立的</h1><p>1、先觉条件</p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln"> </span><span class="com"># git</span></code></li><li class="L1"><code></code></li><li class="L2"><code><span class="pln"> </span><span class="com"># java8</span></code></li><li class="L3"><code></code></li><li class="L4"><code><span class="pln"> </span><span class="com"># mysql 8.0</span></code></li><li class="L5"><code></code></li><li class="L6"><code><span class="pln"> </span><span class="com"># redis 5.0+</span></code></li><li class="L7"><code></code></li><li class="L8"><code><span class="pln"> </span><span class="com"># elasticsearch 7.8.0</span></code></li><li class="L9"><code></code></li><li class="L0"><code><span class="pln"> </span><span class="com"># kafka_2.11-2.5.0</span></code></li><li class="L1"><code></code></li><li class="L2"><code><span class="pln"> </span><span class="com"># nacos 1.3.1</span></code></li></ol></pre><p>注:项目所有配置参数全部由nacos管理，保证了项目参数的保密性，如有需要动态变更的，可以在项目中采用<a href="https://github.com/RefreshScope" title="@RefreshScope" class="at-link">@RefreshScope</a>来达到实时刷新</p> <p>2、项目结构</p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln"> </span><span class="com">#1、主项目 pom 文件</span></code></li><li class="L1"><code><span class="pln"> </span><span class="com">#2、springcloud-gateway-nacos 网关服务 </span></code></li><li class="L2"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln"> com</span><span class="pun">.</span><span class="pln">deng</span><span class="pun">.</span><span class="pln">gateway</span></code></li><li class="L3"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">controller </span><span class="pun">--控制类</span></code></li><li class="L4"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">entity </span><span class="pun">--实体类</span></code></li><li class="L5"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">route </span><span class="pun">--网关路由的配置</span></code></li><li class="L6"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">uitls </span><span class="pun">--网关项目工具包</span></code></li><li class="L7"><code><span class="pln"> </span><span class="pun">└──</span><span class="pln"> pom</span><span class="pun">.</span><span class="pln">xml </span><span class="pun">--子项目</span><span class="pln"> maven </span><span class="pun">配置文件</span></code></li><li class="L8"><code><span class="pln"> </span><span class="com">#3、order 测试服务</span></code></li><li class="L9"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">biz</span><span class="pun">-</span><span class="pln">service </span><span class="pun">---服务子项目</span></code></li><li class="L0"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">client </span><span class="pun">---接口子项目，比如</span><span class="pln">feignclient </span><span class="pun">接口对外暴露</span></code></li><li class="L1"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">common </span><span class="pun">---公共子项目</span></code></li><li class="L2"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">dao </span><span class="pun">---持久层子项目</span></code></li><li class="L3"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">integration </span><span class="pun">---集成配置子项目</span></code></li><li class="L4"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">web </span><span class="pun">---控制层子项目</span></code></li><li class="L5"><code><span class="pln"> </span><span class="pun">├──</span><span class="pln">springcloud</span><span class="pun">-</span><span class="pln">order</span><span class="pun">-</span><span class="pln">start </span><span class="pun">---开启入口子项目</span></code></li><li class="L6"><code><span class="pln"> </span><span class="pun">└──</span><span class="pln">order</span><span class="pun">服务</span><span class="pln">pom</span><span class="pun">文件</span></code></li><li class="L7"><code><span class="pln"> </span><span class="com">#4、nacos_config.zip nacos配置文件</span></code></li></ol></pre><p>3、目前集成的功能</p> <pre class="prettyprint linenums prettyprinted" style=""><ol class="linenums"><li class="L0"><code><span class="pln"> </span><span class="com">#1、mybatis</span></code></li><li class="L1"><code><span class="pln"> </span><span class="com">#2、jwt token</span></code></li><li class="L2"><code><span class="pln"> </span><span class="com">#3、redis</span></code></li><li class="L3"><code><span class="pln"> </span><span class="com">#4、elasticsearch</span></code></li><li class="L4"><code><span class="pln"> </span><span class="com">#5、kafka</span></code></li><li class="L5"><code><span class="pln"> </span><span class="com">#6、nacos gateway动态路由，实时刷新</span></code></li><li class="L6"><code><span class="pln"> </span><span class="com">#7、gateway url白名单实时刷新</span></code></li><li class="L7"><code><span class="pln"> </span><span class="com">#8、gateway hystrix ribbon负载均衡的开启，nacos后台可以调整权重，可以试验同时开启项目中的两个start</span></code></li><li class="L8"><code><span class="pln"> </span><span class="com">#9、feignclient </span></code></li><li class="L9"><code><span class="pln"> </span><span class="com">#10、mybatisplus 代码生成器----start子项目执行类 </span></code></li><li class="L10"><code><span class="pln"> </span><span class="com">#11、统一异常处理</span></code></li><li class="L11"><code><span class="pln"> </span><span class="com">#12、自定义注解---操作日志收集 </span></code></li></ol></pre>
