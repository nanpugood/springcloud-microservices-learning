package com.central.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRole extends SuperEntity{
	 private static final long serialVersionUID = 4497149010220586111L;
	 private String code;
	 private String name;
	 @TableField(exist = false)
	 private Long userId;
}
