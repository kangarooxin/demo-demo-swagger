package com.justfun.common.annotation;

import java.lang.annotation.*;

/**
 * @author pangxin001
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonParam {

    String value() default "";

    boolean urlDecode() default false;

    boolean required() default true;
}
