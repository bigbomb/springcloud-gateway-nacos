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
    SUCCESS("00", "success", 0),

    /**
     * 失败的code
     */
    DATA_ERROR("400003", "数据错误", 2);

    private String code;
    private String message;
    private int level;

    ExceptionTypeEnum(String code, String message, int level) {
        this.code = code;
        this.message = message;
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
