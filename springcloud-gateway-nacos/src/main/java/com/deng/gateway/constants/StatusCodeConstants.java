package com.deng.gateway.constants;

public class StatusCodeConstants {
public static final String TOKEN_NONE = "1001";
/**
 * 返回结果之成功
 */
public static final int STATUS_SUCCESS = 200;

/**
 * 返回accesstoken过期
 */
public static final int ACCESS_TOKEN_EXPIRE = 1001;

/**
 * 返回refreshtoken过期
 */
public static final int REFRESH_TOKEN_EXPIRE = 1002;

/**
 * token校验错误码
 */
public static final int TOKEN_WRONG = 1003;

/**
 * refreshtoken 为空
 */
public static final int REFRESH_TOKEN_EMPTY = 1004;

/**
 * accesstoken 为空
 */
public static final int ACCESS_TOKEN_EMPTY = 1005;
}

