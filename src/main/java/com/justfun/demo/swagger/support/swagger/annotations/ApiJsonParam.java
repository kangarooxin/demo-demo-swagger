package com.justfun.demo.swagger.support.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pangxin001
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonParam {

    String name() default "";

    String value() default "";

    boolean require() default false;

    String type() default "query";

    boolean arrayType() default false;

    Class<?> dataTypeClass() default Void.class;

    Class<?>[] dataTypeParametersClass() default {};
}
