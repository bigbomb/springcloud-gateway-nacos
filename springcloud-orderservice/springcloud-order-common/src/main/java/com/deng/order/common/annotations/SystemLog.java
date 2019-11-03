package com.deng.order.common.annotations;


import java.lang.annotation.*;

import com.deng.order.common.enums.LogTypeEnum;
/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 11:05:32 AM 

* 类说明 

*/
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    /**
     * 日志名称
     *
     * @return
     */
    String description() default "";

    /**
     * 日志类型
     *
     * @return
     */
    LogTypeEnum type() default LogTypeEnum.OPERATION;
}