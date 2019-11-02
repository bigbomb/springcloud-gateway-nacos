package com.deng.gateway.entity;

import com.deng.gateway.entity.Result.ResultBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 

* @author 作者 lujunjie: 

* @version 创建时间：Nov 2, 2019 9:50:05 AM 

* 类说明 

*/
@Data 
@Builder
@NoArgsConstructor  
@AllArgsConstructor  
public class Tokens {

	private String accessToken;
	
	private String refreshToken;
}
