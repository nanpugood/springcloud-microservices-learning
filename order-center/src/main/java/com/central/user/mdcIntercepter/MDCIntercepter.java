package com.central.user.mdcIntercepter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.central.user.constant.Constant;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MDCIntercepter extends HandlerInterceptorAdapter{
	@Trace
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		//生成全局链路日志追踪traceId放入hearder
//        String globalLogPrifix=TraceContext.traceId();
//        if(StringUtils.isEmpty(globalLogPrifix)||globalLogPrifix.equals("Ignored_Trace")){
//            globalLogPrifix=UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
//        }
//        MDC.put(Constant.GLOBAL_LOG_PRIFIX, globalLogPrifix);
//        log.info("***过滤器中放入traceId的值***"+globalLogPrifix);
        String traceId=request.getHeader(Constant.GLOBAL_LOG_PRIFIX);
        boolean isBindTraceId =false;
        try {
        	log.info("***order 服务中获取traceId的值***"+traceId);
            isBindTraceId=bindTrace(traceId);
            //TODO 权限token验证
        }finally {
//            if(isBindTraceId){
//                MDC.remove(Constant.GLOBAL_LOG_PRIFIX);
//            }
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    /**
     * 将traceId绑定到MDC
     */
    private boolean bindTrace(String traceId){
        if(StringUtils.isNotEmpty(traceId)){
            MDC.put(Constant.GLOBAL_LOG_PRIFIX, traceId);
            return true;
        }
        return false;
    }

}
