package com.justfun.demo.swagger.support.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.google.common.base.Optional;

import com.justfun.demo.swagger.support.swagger.annotations.ApiJsonParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import static springfox.documentation.schema.Collections.*;
import static springfox.documentation.service.Parameter.DEFAULT_PRECEDENCE;
import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

/**
 * @author pangxin001
 */
@Component
@Order
public class JsonParamReader implements ParameterBuilderPlugin {

    @Autowired
    private TypeNameExtractor typeNameExtractor;

    @Autowired
    private TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext context) {

        ResolvedMethodParameter methodParameter = context.resolvedMethodParameter();

        Optional<ApiJsonParam> optional = methodParameter.findAnnotation(ApiJsonParam.class);  //根据参数上的ApiJsonObject注解中的参数动态生成Class
        if (!optional.isPresent()) {
            return;
        }

        ApiJsonParam anno = optional.get();

        ModelReference itemModel = null;

        String name = anno.name();
        if ("".equals(name)) {
            name = methodParameter.defaultName().orNull();
        }

        ResolvedType modelType;
        if(anno.dataTypeClass().isAssignableFrom(Void.class)) {
            modelType = methodParameter.getParameterType();
        } else if(anno.arrayType()) {
            modelType = typeResolver.arrayType(typeResolver.resolve(anno.dataTypeClass(), anno.dataTypeParametersClass()));
        } else {
            modelType = typeResolver.resolve(anno.dataTypeClass(), anno.dataTypeParametersClass());
        }

        String typeName;
        if (isContainerType(modelType)) {
            ResolvedType elementType = collectionElementType(modelType);
            String itemTypeName = typeNameFor(context, elementType);
            typeName = containerType(modelType);
            itemModel = new ModelRef(itemTypeName);
        } else {
            typeName = typeNameFor(context, modelType);
        }

        context.parameterBuilder()
                .name(name)
                .description(anno.value())
                .defaultValue(null)
                .required(anno.require())
                .allowMultiple(isContainerType(modelType))
                .type(modelType)
                .modelRef(new ModelRef(typeName, itemModel))
                .parameterType(anno.type())
                .order(DEFAULT_PRECEDENCE)
                .parameterAccess(null);
    }

    private String typeNameFor(ParameterContext context, ResolvedType parameterType) {
        ModelContext modelContext = inputParam(
                context.getGroupName(),
                parameterType,
                context.getDocumentationType(),
                context.getAlternateTypeProvider(),
                context.getGenericNamingStrategy(),
                context.getIgnorableParameterTypes());
        return typeNameExtractor.typeName(modelContext);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
