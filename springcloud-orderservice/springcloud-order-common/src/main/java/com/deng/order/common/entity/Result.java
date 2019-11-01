package com.deng.order.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor  
@AllArgsConstructor  
public class Result<T> {  
    //0 成功，非0 失败  
    private int code;  
  
    //响应提示信息  
    private String message;  
  
    //响应实体  
    private T body;  
  
    public Result(int code, String message) {  
        this.code = code;  
        this.message = message;  
    }  
}  