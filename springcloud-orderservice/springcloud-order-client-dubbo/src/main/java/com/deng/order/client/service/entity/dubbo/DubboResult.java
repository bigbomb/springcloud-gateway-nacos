package com.deng.order.client.service.entity.dubbo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data 
@Builder
@NoArgsConstructor  
@AllArgsConstructor  
public class DubboResult<T> implements Serializable {
    //0 成功，非0 失败  
    private int code;  
  
    //响应提示信息  
    private String message;  
  
    //响应实体  
    private T body;  

}  