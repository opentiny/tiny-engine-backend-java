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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Title: SystemLogAspect
 *
 * @version V1.0  Description: 切点类
 * @date 2024年7月30日
 */
@Aspect
@Component
@SuppressWarnings("all")
@Slf4j
public class SystemLogAspect {

    // 本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    // ThreadLocal 变量用于存储日志状态
    private ThreadLocal<Boolean> alreadyLogged = ThreadLocal.withInitial(() -> false);

    /**
     * Gets service method description.
     *
     * @param joinPoint the join point
     * @return the service method description
     * @throws Exception the exception
     * @Description 获取注解中对方法的描述信息 用于service层注解
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
     * Gets controller method description.
     *
     * @param joinPoint the join point
     * @return the controller method description
     * @throws Exception the exception
     * @Description 获取注解中对方法的描述信息 用于Controller层注解
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
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
    // Service层切点
    @Pointcut("@annotation(com.tinyengine.it.config.log.SystemServiceLog)")
    public void serviceAspect() {
    }

    /**
     * Controller aspect.
     */
    // Controller层切点
    @Pointcut("@annotation(com.tinyengine.it.config.log.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * Do before.
     *
     * @param joinPoint the join point
     * @Description 前置通知 用于拦截Controller层记录用户的操作
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request =
            ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        try {
            logger.info("请求方法: {}",
                (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            logger.info("方法描述: {}", getControllerMethodDescription(joinPoint));
        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息：{}", e.getMessage());
        }
    }

    /**
     * Do after throwing.
     *
     * @param joinPoint the join point
     * @param e         the e
     * @throws JsonProcessingException the json processing exception
     * @Description 异常通知 用于拦截service层记录异常日志
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws JsonProcessingException {
        HttpServletRequest request =
            ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        try {
            logger.error("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature()
                .getName() + "()"));
            logger.error("方法描述:" + getServiceMethodDescription(joinPoint));
            logger.error("异常代码:" + e.getClass().getName());
            logger.error("异常信息:" + e.getMessage());
            logger.error("异常堆栈信息:");
            for (StackTraceElement element : e.getStackTrace()) {
                logger.error("\t" + element.toString());
            }

        } catch (Exception ex) {
            // 记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
    }

}
