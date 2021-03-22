package com.central.user.controller;


import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.central.user.feign.RemoteFeignClient;
import com.central.user.model.SysUser;
import com.central.user.service.SysUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * 用户
 */
@Slf4j
@RestController
public class SysUserController {
	
	@Autowired
	private RemoteFeignClient removeFeignClient;
	@Autowired
	private SysUserService sysUserService;
	
	
	//@Trace
    @GetMapping(value = "/users-anon/login")
    public Object findByUsername() {
		String userName = "admin";
		/**
		 * 
		 * TraceContext.traceId() 这个api比较特殊，是依赖sw的环境的。
		 *   如果部署改服务的环境中存在sw服务，则此API会有值，如果没有部署sw的环境，则此api的值为空！！！
		 *   也就是单机版是获取不到值的，在服务器上部署后，对应内容才有值。这个是也是因为@trace 注解起作用
		 * 
		 */
//		String globalLogPrifix=TraceContext.traceId();
//		if(StringUtils.isBlank(globalLogPrifix)) {
//			 globalLogPrifix = "2222222";
//		}
//		log.info("traceId的值为{}",globalLogPrifix);
//		MDC.put("GLOBAL_LOG_PRIFIX",globalLogPrifix);
//		log.info("执行方法，日志级别为info，参数为:{}",userName);
//		System.out.println("**日志级别执行完成**");
    	SysUser sysUser = sysUserService.selectByUsername(userName);
        return sysUser;
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    public Object findByMobile(String mobile) {
    	log.info("***调用Mobile方法***"+mobile+"*****");
    	sysUserService.findByMobile();
    	log.info("******开始调用远程方法*****");
    	removeFeignClient.findByMobile();
        return "成功";
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    public Object findByOpenId(String openId) {
//    	 String globalLogPrifix=TraceContext.traceId();
//		 if(StringUtils.isBlank(globalLogPrifix)) {
//			 globalLogPrifix = "111111";
//		 }
//		 log.info("traceId的值为{}",globalLogPrifix);
//		 MDC.put("GLOBAL_LOG_PRIFIX",globalLogPrifix);
//		 log.info("执行方法，日志级别为info，参数为:{}",openId);
//		 System.out.println("**日志级别执行完成**");
//    	
//    	 log.info("执行方法，日志级别为info，参数为:{}",openId);
//    	 System.out.println("**日志级别执行完成**");
    	 
    	 removeFeignClient.createOrder();
    	 
        return "成功";
    }

}
