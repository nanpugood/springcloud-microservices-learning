package com.central.user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.user.mapper.SysUserMapper;
import com.central.user.model.SysUser;
import com.central.user.service.SysUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Override
	public SysUser selectByUsername(String username) {
		List<SysUser> users = baseMapper.selectList(new QueryWrapper<SysUser>().eq("username", username) );
		return getUser(users);
	}
	
   private SysUser getUser(List<SysUser> users) {
        SysUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

	@Override
	public void findByMobile() {
		log.info("services层调用方法{}","findByMobile");
	}

}
