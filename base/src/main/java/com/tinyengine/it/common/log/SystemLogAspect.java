/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.common.log;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.AnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Title: SystemLogAspect
 *
 * @version V1.0 Description: 切点类
 * @since 2024-10-20
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {
    // 本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    // ThreadLocal 变量用于存储日志状态
    private final ThreadLocal<Boolean> alreadyLogged = ThreadLocal.withInitial(() -> false);

    /**
     * Gets service method description. 获取注解中对方法的描述信息 用于service层注解
     *
     * @param joinPoint the join point
     * @return the service method description
     * @throws Exception the exception
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception {
        SystemServiceLog methodAnnotation = getMethodAnnotation(joinPoint, SystemServiceLog.class);
        if (methodAnnotation == null) {
            return "";
        }
        return methodAnnotation.description();
    }

    private static <T extends Annotation> T getMethodAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        // 确保 JoinPoint 是方法签名类型
        if (!(joinPoint.getSignature() instanceof MethodSignature)) {
            throw new AnnotationException("No method signature found.");
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 检查方法是否为 null
        if (method == null) {
            throw new AnnotationException("Method is null.");
        }

        // 获取并返回注解
        return method.getAnnotation(annotationClass);
    }

    /**
     * Gets controller method description. 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint the join point
     * @return the controller method description
     * @throws Exception the exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        SystemControllerLog methodAnnotation = getMethodAnnotation(joinPoint, SystemControllerLog.class);
        if (methodAnnotation == null) {
            return "";
        }
        return methodAnnotation.description();
    }

    /**
     * Service aspect.
     */
    @Pointcut("@annotation(com.tinyengine.it.common.log.SystemServiceLog)")
    public void serviceAspect() {
        log.debug("service aspect");
    }

    /**
     * Controller aspect.
     */
    @Pointcut("@annotation(com.tinyengine.it.common.log.SystemControllerLog)")
    public void controllerAspect() {
        log.debug("controller aspect");
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint the join point
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        logger.debug("Method: {}", joinPoint.getSignature().getName());
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint the join point
     * @param e         the e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Method: {}", joinPoint.getSignature().getName());
        logger.error("Exception Message:" + e.getMessage());
    }
}
