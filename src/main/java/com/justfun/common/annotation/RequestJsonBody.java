package com.justfun.common.annotation;

import java.lang.annotation.*;

/**
 * @author pangxin001
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonBody {

    boolean required() default true;
}
