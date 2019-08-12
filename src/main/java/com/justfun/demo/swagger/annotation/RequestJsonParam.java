package com.justfun.demo.swagger.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonParam {

    String value() default "";

    boolean urlDecode() default false;

    boolean required() default true;
}
