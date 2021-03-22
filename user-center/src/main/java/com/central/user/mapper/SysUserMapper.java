package com.central.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.central.user.config.SuperMapper;
import com.central.user.model.SysUser;

@Mapper
public interface SysUserMapper extends SuperMapper<SysUser>{

}
