package com.central.user.mdcIntercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired
	private MDCIntercepter mdcIntercepter;
	
	/**
	 * 添加interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(mdcIntercepter).addPathPatterns("/**").excludePathPatterns("/actuator/**","");
	}

}
