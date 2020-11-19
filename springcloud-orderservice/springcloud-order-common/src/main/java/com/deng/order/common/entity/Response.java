package com.deng.order.common.entity;

import com.deng.order.common.enums.ExceptionTypeEnum;
import lombok.*;

@Data
@Builder
@NoArgsConstructor  
@AllArgsConstructor  
public class Response<T> {
   /**

    */
    private int code;

    /**

     */
    private String message;

    /**

     */
    private T data;
    public Response(T data) {
        this.code = ExceptionTypeEnum.SUCCESS.getCode();
        this.message = ExceptionTypeEnum.SUCCESS.getMessage();
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}  