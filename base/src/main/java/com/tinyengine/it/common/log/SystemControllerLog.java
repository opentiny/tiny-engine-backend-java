package com.tinyengine.it.common.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface System controller log.
 *
 * @version V1.0 Description: 自定义注解，拦截controller
 * @since 2024-10-20
 */
@Target({ElementType.PARAMETER, ElementType.METHOD}) // 作用在参数和方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时注解
@Documented // 表明这个注解应该被 javadoc工具记录
public @interface SystemControllerLog {
    /**
     * description
     *
     * @return {@link String }
     */
    String description() default "";
}
