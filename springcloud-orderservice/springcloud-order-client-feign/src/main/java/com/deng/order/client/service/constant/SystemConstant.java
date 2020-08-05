package com.deng.order.client.service.constant;

public class SystemConstant {

	/**
	 * 返回结果之成功
	 */
	public static final int RESULT_CODE_SUCCESS = 200;

	/**
	 * 返回结果之失败
	 */
	public static final int RESULT_CODE_FAILURE = 500;
	
	
	/**
	 * 返回结果失败之服务无法连接
	 */
	public static final String RESULT_SERVICE_FAILURE= "您的请求被限流了";
	
	
	/**
	 * 返回结果之服务连接成功
	 */
	public static final String RESULT_SERVICE_SUCCESS= "接口服务可用";
}
