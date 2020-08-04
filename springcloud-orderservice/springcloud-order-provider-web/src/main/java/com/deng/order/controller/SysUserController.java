package com.deng.order.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.deng.order.common.annotations.SystemLog;
import com.deng.order.common.constant.SystemConstant;
import com.deng.order.common.entity.Result;
import com.deng.order.common.entity.SysUser;
import com.deng.order.common.enums.LogTypeEnum;
import com.deng.order.service.SysUserService;
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
	@Autowired
	private SysUserService sysUserService;
	
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 测试日志自定义注解的运行
     * @return
     */
	@GetMapping("list")
    @SystemLog(description = "查询用户列表", type = LogTypeEnum.OPERATION)
    public Result<Object> test2(){
//    	List<SysUser> userlist = sysUserService.list();
		List<SysUser> userlist = new ArrayList<SysUser>();
		SysUser sysUser = SysUser.builder()
				.username("1")
				.password("123456")
				.nickname("test")
				.build();
		userlist.add(sysUser);
    	Result<Object> result = null;
		try {
			result = Result.builder()
					.code(SystemConstant.RESULT_CODE_SUCCESS)
					.message(SystemConstant.RESULT_SERVICE_SUCCESS)
					.body(objectMapper.writeValueAsString(userlist))
					.build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
}
