package com.loushi.component.aop;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.loushi.util.AjaxResult;
import com.loushi.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * controlelr 日志
 * @author 技术部
 */

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(public * com.loushi.controller..*.*(..))")
    public void webLog() {
    }


    /**
     * 在切点之前织入
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        logger.info("<-- {}, {}, {}, {}.{}, {}",
                request.getMethod(),
                ServletUtil.getParamMap(request),
                request.getRequestURI(),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                IPUtil.getIpAddress(request));
    }


    /**
     * 使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
     */
    @AfterReturning(returning = "result", pointcut = "webLog()")
    public void doAfterReturning(AjaxResult result) {
        Object data = result.get("data");

        //这里判断分页参数对象
        if (data instanceof PageInfo) {
            PageInfo pageInfo = (PageInfo) data;
            log.info("--> 响应RESPONSE : {}", AjaxResult.success(new ArrayList<>(pageInfo.getList())));
        } else {
            log.info("--> 响应RESPONSE : {}", result);
        }
    }

}
