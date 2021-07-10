package com.deng.order.common.exception;

import java.io.IOException;


import com.deng.order.common.entity.Response;
import com.deng.order.common.enums.ExceptionTypeEnum;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 10:32:54 AM 

* 类说明 

*/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BusinessException.class)
    public Response businessExceptionHandler(BusinessException exception) throws IOException {
        logger.info(exception.toString());
        Response response = Response.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    public Response otherExceptionHandler(Exception e) throws IOException {
        logger.error(e.toString());
        Response response = Response.builder()
                .code(10000)
                .message("网络正忙，请稍后再试")
                .build();
        return response;
    }
    @ExceptionHandler(value = RpcException.class)
    public Response flowExceptionHandler(Exception e){
        logger.error(e.toString());
        Response response = Response.builder()
                .code(500)
                .message("您的请求被限流了")
                .build();
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return new Response<>(ExceptionTypeEnum.VALIDATE_FAILED.getCode(), objectError.getDefaultMessage());
    }
}
