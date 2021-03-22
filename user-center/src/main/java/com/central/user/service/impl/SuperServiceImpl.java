package com.central.user.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.central.user.service.ISuperService;

public class SuperServiceImpl <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T>{

}
