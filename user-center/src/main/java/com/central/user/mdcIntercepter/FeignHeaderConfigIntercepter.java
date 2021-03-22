package com.central.user.mdcIntercepter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.central.user.constant.Constant;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign传递token
 * 在微服务内部调用时，统一使用feign远程调用，
 * 只需要在对应微服务配置本类，自动在请求头加上
 * 授权token和本次全局链路请求的全局日志头
 */
@Slf4j
@Configuration
public class FeignHeaderConfigIntercepter implements RequestInterceptor{

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //添加全局日志前缀
        String traceId = MDC.get(Constant.GLOBAL_LOG_PRIFIX);
        requestTemplate.header(Constant.GLOBAL_LOG_PRIFIX, traceId);
        //traceId放入log4j2的MDC
        //MDC.put(Constant.GLOBAL_LOG_PRIFIX, request.getHeader(Constant.GLOBAL_LOG_PRIFIX));
        log.info("**user-center服务调用feign请求拦截器**");
	}

}
