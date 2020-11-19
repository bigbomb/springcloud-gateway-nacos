package com.deng.order.common.enums;
/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 3, 2019 10:40:29 AM 

* 类说明 

*/
public enum ExceptionTypeEnum {
    /**
     * 成功的code
     */
    SUCCESS(1000, "成功",1),
    /**
     * 失败的code
     */
    FAILED(1001, "响应失败",2),
    /**
     * 校验失败的code
     */
    VALIDATE_FAILED(1002, "参数校验失败",3),
    /**
     * 位置错误的code
     */
    ERROR(5000, "未知错误",4);

    private int code;
    private String message;
    private int level;

    ExceptionTypeEnum(int code, String message, int level) {
        this.code = code;
        this.message = message;
        this.level = level;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
