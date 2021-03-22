package com.central.user.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * 用户
 */
@Slf4j
@RestController
public class OrderController {

    /**
     * 查询用户登录对象LoginAppUser
     */
    @GetMapping(value = "/order/create")
    public Object create() {
    	log.info("order服务调用成功***create 方法");
        return "成功";
    }
    
    @GetMapping(value = "/order/findByMobile")
    public Object findByMobile() {
    	log.info("order服务调用成功findByMobile方法");
        return "成功";
    }



}
