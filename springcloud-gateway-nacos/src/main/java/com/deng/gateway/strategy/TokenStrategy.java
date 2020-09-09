package com.deng.gateway.strategy;

import com.deng.gateway.entity.Result;

public interface TokenStrategy {
	public Result checkisBlank(String token, String username);

}
