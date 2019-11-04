package com.deng.order.common.exception;

import com.deng.order.common.enums.ExceptionTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private String level;
	
	public BusinessException(ExceptionTypeEnum exceptionTypeEnum)
	{
		 this.code = exceptionTypeEnum.getCode();
		 this.message= exceptionTypeEnum.getMessage();	
	}
}
