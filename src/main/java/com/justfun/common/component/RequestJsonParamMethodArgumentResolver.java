package com.justfun.common.component;

import com.justfun.common.annotation.RequestJsonParam;
import com.justfun.common.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class RequestJsonParamMethodArgumentResolver extends AnnotationMethodArgumentResolver<RequestJsonParam> {

    public RequestJsonParamMethodArgumentResolver() {
        super(RequestJsonParam.class);
    }

    @Override
    protected Object doResolveArgument(HttpServletRequest request, HttpServletResponse response, RequestJsonParam anno, MethodParameter parameter) throws Exception {
        String paramName = anno.value();
        if(StringUtils.isEmpty(paramName)) {
            paramName = parameter.getParameterName();
        }
        String value = request.getParameter(paramName);
        if(!StringUtils.isEmpty(value)) {
            if(anno.urlDecode()) {
                value = URLEncoder.encode(value, "UTF-8");
            }
            Object ret = JsonUtils.parse(value, JsonUtils.constructJavaType(parameter.getGenericParameterType()));
            if(ret == null) {
                throw new MethodArgumentTypeMismatchException(value, parameter.getParameterType(), paramName, parameter, null);
            }
            return ret;
        }
        if(anno.required()) {
            throw new MissingServletRequestParameterException(paramName,  parameter.getParameterType().getName());
        }
        return null;
    }
}
