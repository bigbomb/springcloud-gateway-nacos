package com.deng.order.common.exception;

import java.io.IOException;

import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSONObject;

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 10:32:54 AM 

* 类说明 

*/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BusinessException.class)
    public JSONObject businessExceptionHandler(BusinessException exception) throws IOException {
        logger.info(exception.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", exception.getCode());
        jsonObject.put("message", exception.getMessage());
        return jsonObject;
    }

    @ExceptionHandler(value = Exception.class)
    public JSONObject otherExceptionHandler(Exception e) throws IOException {
        logger.error("服务提供者有异常！");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "10000");
        jsonObject.put("message", "网络正忙，请稍后再试");
        return jsonObject;
    }
    @ExceptionHandler(value = RpcException.class)
    public JSONObject flowExceptionHandler(Exception e){
        logger.error(e.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "500");
        jsonObject.put("message", "您的请求被限流了");
        return jsonObject;
    }
}
