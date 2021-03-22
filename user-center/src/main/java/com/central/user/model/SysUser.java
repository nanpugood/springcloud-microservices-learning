package com.central.user.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser extends SuperEntity{
	
	private String username;
	private String password;
	private String nickname;
	private String headImgUrl;
	private String mobile;
	private Integer sex;
	private Boolean enabled;
	private String type;
	private String openId;
	@TableLogic
	private boolean isDel;

	@TableField(exist = false)
	private List<SysRole> roles;
	@TableField(exist = false)
	private String roleId;
	@TableField(exist = false)
	private String oldPassword;
	@TableField(exist = false)
	private String newPassword;
}
