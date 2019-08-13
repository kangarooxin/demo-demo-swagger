package com.justfun.common.component;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author kangarooxin
 */
public abstract class AnnotationMethodArgumentResolver<T extends Annotation> implements HandlerMethodArgumentResolver {

    private Class<T> clazz;

    public AnnotationMethodArgumentResolver(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(this.clazz)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        T annotation = parameter.getParameterAnnotation(this.clazz);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        return doResolveArgument(request, response, annotation, parameter);
    }

    protected abstract Object doResolveArgument(HttpServletRequest request, HttpServletResponse response, T annotation, MethodParameter parameter) throws Exception;

}
