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

@Slf4j
@Configuration
public class FeignHeaderConfigIntercepter implements RequestInterceptor{
	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = attributes.getRequest();
	    //添加全局日志前缀
	    requestTemplate.header(Constant.GLOBAL_LOG_PRIFIX, request.getHeader(Constant.GLOBAL_LOG_PRIFIX));
	    //traceId放入log4j2的MDC
	    MDC.put(Constant.GLOBAL_LOG_PRIFIX, request.getHeader(Constant.GLOBAL_LOG_PRIFIX));
	    
	    log.info("**order服务中调用feign请求拦截器**");
	}

}
