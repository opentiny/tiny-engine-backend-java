package com.tinyengine.it.config.log;

import java.lang.annotation.*;

/**
 * The interface System service log.
 *
 * @version V1.0  Description:  自定义注解，拦截service
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
