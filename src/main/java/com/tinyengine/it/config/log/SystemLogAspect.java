
package com.tinyengine.it.config.log;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * Gets controller method description. 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint the join point
     * @return the controller method description
     * @throws Exception the exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        // 目标方法名
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * Service aspect.
     */
    @Pointcut("@annotation(com.tinyengine.it.config.log.SystemServiceLog)")
    public void serviceAspect() {
        log.debug("service aspect");
    }

    /**
     * Controller aspect.
     */
    @Pointcut("@annotation(com.tinyengine.it.config.log.SystemControllerLog)")
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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        HttpSession session = request.getSession();

        try {
            logger.info("Method: {}",
                    (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            logger.info("Method Description: {}", getControllerMethodDescription(joinPoint));
        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("Exception Message：{}", e.getMessage());
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint the join point
     * @param e         the e
     * @throws JsonProcessingException the json processing exception
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        HttpSession session = request.getSession();
        try {
            logger.error("Method:"
                    + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            logger.error("Method Description:" + getServiceMethodDescription(joinPoint));
            logger.error("Exception ClassName:" + e.getClass().getName());
            logger.error("Exception Message:" + e.getMessage());
            logger.error("Exception Message:" + e.getMessage());
            logger.error("Exception Stack:");
            for (StackTraceElement element : e.getStackTrace()) {
                logger.error("\t" + element.toString());
            }
        } catch (Exception ex) {
            // 记录本地异常日志
            logger.error("Exception Message:{}", ex.getMessage());
        }
    }
}
