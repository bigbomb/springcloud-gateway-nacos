package com.deng.order.controller;


import com.deng.order.common.entity.User;
import com.deng.order.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.deng.order.common.annotations.SystemLog;
import com.deng.order.common.constant.SystemConstant;
import com.deng.order.common.entity.Response;
import com.deng.order.common.enums.LogTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * 运营后台用户表 前端控制器
 * </p>
 *
 * @author bigbomb
 * @since 2019-11-03
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {
	private static final Logger LOG = LoggerFactory.getLogger(SysUserController.class);
	@Autowired
	private UserService userService;
	
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 测试日志自定义注解的运行
     * @return
     */
	@GetMapping("list")
    @SystemLog(description = "查询用户列表", type = LogTypeEnum.OPERATION)
    public Response<Object> test2(){
		User sysUser = User.builder()
				.nickname("test")
				.age("18")
				.build();
		userService.save(sysUser);
		LOG.info("feignclient调用结束");
    	Response<Object> result = null;
		try {
			result = Response.builder()
					.code(SystemConstant.RESULT_CODE_SUCCESS)
					.message(SystemConstant.RESULT_SERVICE_SUCCESS)
					.data(objectMapper.writeValueAsString(sysUser))
					.build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
}
