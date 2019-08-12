package com.justfun.common.component;

import com.justfun.common.annotation.RequestJsonBody;
import com.justfun.common.util.HttpServletUtils;
import com.justfun.common.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pangxin001
 */
public class RequestJsonBodyMethodArgumentResolver extends AnnotationMethodArgumentResolver<RequestJsonBody> {

    public RequestJsonBodyMethodArgumentResolver() {
        super(RequestJsonBody.class);
    }

    @Override
    protected Object doResolveArgument(HttpServletRequest request, HttpServletResponse response, RequestJsonBody annotation, MethodParameter parameter) throws Exception {
        String paramName = parameter.getParameterName();
        String content = HttpServletUtils.getRequestContent(request);
        if(!StringUtils.isEmpty(content)) {
            Object ret = JsonUtils.parse(content, JsonUtils.constructJavaType(parameter.getGenericParameterType()));
            if(ret == null) {
                throw new MethodArgumentTypeMismatchException(content, parameter.getParameterType(), paramName, parameter, null);
            }
            return ret;
        }
        if(annotation.required()) {
            throw new MissingServletRequestParameterException(paramName, parameter.getParameterType().getName());
        }
        return null;
    }
}
