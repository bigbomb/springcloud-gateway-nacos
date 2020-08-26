package com.deng.order.common.aop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import com.deng.order.common.annotations.SystemLog;
import com.deng.order.common.entity.Log;
import com.deng.order.common.utils.IpInfoUtil;
import com.deng.order.common.utils.ObjectUtil;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 11:09:57 AM 

* 类说明 

*/
@Aspect
@Component
public class SystemLogAspect {
	private static final ThreadLocal<Date> BEGINTIME_THREADLOCAL = new NamedThreadLocal<Date>("ThreadLocal beginTime");

	 private static Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
    @Pointcut("@annotation(com.deng.order.common.annotations.SystemLog)")
    public void controllerAspect() {

    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {

        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        BEGINTIME_THREADLOCAL.set(beginTime);
    }


    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            String username = "";
            String description = getControllerMethodInfo(joinPoint).get("description").toString();
            Map<String, String[]> requestParams = request.getParameterMap();

            Log log = new Log();

            //请求用户
            //后台操作(非登录)
//            if (Objects.equals(getControllerMethodInfo(joinPoint).get("type"), 0)) {
//                //后台操作请求(已登录)
//                User user = userService.getLoginUser(request);
//                if (user != null) {
//                    username = user.getUsername();
//                }
//                log.setUsername(username);
//            }
            log.setUsername("bigbomb");
            //日志标题
            log.setName(description);
            //日志类型
            log.setLogType((int) getControllerMethodInfo(joinPoint).get("type"));
            //日志请求url
            log.setRequestUrl(request.getRequestURI());
            //请求方式
            log.setRequestType(request.getMethod());
            //请求参数
            log.setRequestParam(ObjectUtil.mapToString(requestParams));

            //其他属性
            log.setIp(IpInfoUtil.getIpAddr(request));
            log.setCreateBy("system");
            log.setUpdateBy("system");
            log.setCreateTime(new Date());
            log.setUpdateTime(new Date());
            log.setDelFlag(0);

            //.......
            //请求开始时间
            long beginTime = BEGINTIME_THREADLOCAL.get().getTime();
            long endTime = System.currentTimeMillis();
            //请求耗时
            Long logElapsedTime = endTime - beginTime;
            log.setCostTime(logElapsedTime.intValue());
            logger.info("日志测试为："+log.toString());
            BEGINTIME_THREADLOCAL.remove();
            //持久化(存储到数据或者ES，可以考虑用线程池)
            //logService.insert(log);
//            ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log, logService));

        } catch (Exception e) {
        	logger.error("AOP后置通知异常", e);
        }
    }


//    /**
//     * 保存日志至数据库
//     */
//    private static class SaveSystemLogThread implements Runnable {
//
//        private Log log;
//        private LogService logService;
//
//        public SaveSystemLogThread(Log esLog, LogService logService) {
//            this.log = esLog;
//            this.logService = logService;
//        }
//
//        @Override
//        public void run() {
//            logService.insert(log);
//        }
//    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static Map<String, Object> getControllerMethodInfo(JoinPoint joinPoint) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>(16);
        //获取目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        //获取相关参数
        Object[] arguments = joinPoint.getArgs();
        //生成类对象
        Class targetClass = Class.forName(targetName);
        //获取该类中的方法
        Method[] methods = targetClass.getMethods();

        String description = "";
        Integer type = null;

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class[] clazzs = method.getParameterTypes();
            if (clazzs.length != arguments.length) {
                //比较方法中参数个数与从切点中获取的参数个数是否相同，原因是方法可以重载哦
                continue;
            }
            description = method.getAnnotation(SystemLog.class).description();
            type = method.getAnnotation(SystemLog.class).type().ordinal();
            map.put("description", description);
            map.put("type", type);
        }
        return map;
    }
}
