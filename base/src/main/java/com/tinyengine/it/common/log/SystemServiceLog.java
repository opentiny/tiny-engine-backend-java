package com.tinyengine.it.common.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface System service log.
 *
 * @version V1.0 Description: 自定义注解，拦截service
 * @since 2024-10-20
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemServiceLog {
    /**
     * Description string.
     *
     * @return the string
     */
    String description() default "";
}
