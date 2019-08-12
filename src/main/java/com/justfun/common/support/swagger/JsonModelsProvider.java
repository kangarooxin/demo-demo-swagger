package com.justfun.common.support.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.justfun.common.support.swagger.annotations.ApiJsonParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.util.List;

/**
 * @author pangxin001
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JsonModelsProvider implements OperationModelsProviderPlugin {

    @Autowired
    private TypeResolver typeResolver;

    @Override
    public void apply(RequestMappingContext context) {
        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        for (ResolvedMethodParameter parameterType : parameterTypes) {
            Optional<ApiJsonParam> optional = parameterType.findAnnotation(ApiJsonParam.class);
            if(!optional.isPresent()) {
                continue;
            }
            ApiJsonParam anno = optional.get();
            ResolvedType modelType;
            if(anno.dataTypeClass().isAssignableFrom(Void.class)) {
                modelType = context.alternateFor(parameterType.getParameterType());
            } else {
                modelType = typeResolver.resolve(anno.dataTypeClass(), anno.dataTypeParametersClass());
            }
            context.operationModelsBuilder().addInputParam(modelType);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
