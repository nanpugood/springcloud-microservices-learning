package com.central.user.service;

import com.central.user.model.SysUser;

public interface SysUserService {

	SysUser selectByUsername(String username);

	void findByMobile();
	
	
}
