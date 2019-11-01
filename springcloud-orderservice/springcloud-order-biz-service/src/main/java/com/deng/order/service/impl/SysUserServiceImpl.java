package com.deng.order.service.impl;

import com.deng.order.common.entity.SysUser;
import com.deng.order.dao.SysUserMapper;
import com.deng.order.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运营后台用户表 服务实现类
 * </p>
 *
 * @author bigbomb
 * @since 2019-11-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
