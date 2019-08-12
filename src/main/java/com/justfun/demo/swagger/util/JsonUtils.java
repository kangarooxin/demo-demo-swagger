package com.justfun.demo.swagger.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T parse(String jsonStr, Class<T> valueType) {
        try {
            if(!StringUtils.isEmpty(jsonStr)) {
                return mapper.readValue(jsonStr, valueType);
            }
        } catch (Exception e) {
            log.error("parse failed!" + e.getMessage());
        }
        return null;
    }

    public static <T> T parse(String jsonStr, JavaType valueType) {
        try {
            if(!StringUtils.isEmpty(jsonStr)) {
                return mapper.readValue(jsonStr, valueType);
            }
        } catch (Exception e) {
            log.error("parse failed!" + e.getMessage());
        }
        return null;
    }

    public static <T> T parse(String jsonStr, TypeReference<?> valueTypeRef) {
        try {
            if(!StringUtils.isEmpty(jsonStr)) {
                return mapper.readValue(jsonStr, valueTypeRef);
            }
        } catch (Exception e) {
            log.error("parse failed!" + e.getMessage());
        }
        return null;
    }

    public static <T> T parse(JsonParser jp, TypeReference<?> valueTypeRef) {
        try {
            return mapper.readValue(jp, valueTypeRef);
        } catch (Exception e) {
            log.error("parse failed!" + e.getMessage());
        }
        return null;
    }

    /**
     * 解析json string为map对象
     *
     * @param str
     * @return
     */
    public static Map<String, String> parseToMap(String str) {
        return parse(str, mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
    }

    /**
     * 解析json string为map对象
     *
     * @param str
     * @return
     */
    public static <K, V> Map<K, V> parseToMap(String str, Class<K> keyClass, Class<V> valueClass) {
        return parse(str, mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
    }

    /**
     * 解析json string为map对象
     *
     * @param str
     * @return
     */
    public static <K, V> Map<K, V> parseToMap(String str, JavaType keyClass, JavaType valueClass) {
        return parse(str, mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
    }

    /**
     * 解析json string为map对象
     *
     * @param str
     * @return
     */
    public static <K, V> Map<K, List<V>> parseToMapList(String str, Class<K> keyClass, Class<V> valueClass) {
        return parse(str, mapper.getTypeFactory().constructMapType(Map.class, constructJavaType(keyClass), constructCollectionType(ArrayList.class, valueClass)));
    }

    /**
     * 解析json string为list对象
     *
     * @return
     */
    public static <T> List<T> parseToList(String str, Class<T> clazz) {
        return parse(str, mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
    }

    /**
     * 解析json string为集合对象
     *
     * @return
     */
    public static <T> Object parseToCollection(String str, Class<? extends Collection> collectionClass, Class<T> clazz) {
        return parse(str, mapper.getTypeFactory().constructCollectionType(collectionClass, clazz));
    }

    /**
     * 构造泛型java类型
     *
     * @param genericClass
     * @param parameterClasses
     * @return
     */
    public static JavaType constructGenericType(Class<?> genericClass, Class<?> parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses);
    }

    /**
     * 构造泛型java类型
     *
     * @param genericClass
     * @param parameterClasses
     * @return
     */
    public static JavaType constructGenericType(Class<?> genericClass, JavaType parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses);
    }


    /**
     * 构造泛型java类型
     *
     * @param type
     * @return
     */
    public static JavaType constructJavaType(Type type) {
        return mapper.getTypeFactory().constructType(type);
    }


    /**
     * 构造集合类型
     *
     * @return
     */
    public static JavaType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 构造简单java类型
     *
     * @return
     */
    public static JavaType constructJavaType(Class<?> clazz) {
        return mapper.getTypeFactory().constructType(clazz);
    }

    /**
     *
     * @return
     */
    public static TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }

    /**
     * object对象转换给json string
     *
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        String retStr = "";
        if (obj == null) {
            return retStr;
        }
        try {
            retStr = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Object to json string failed!", e);
        }
        return retStr;
    }

    public static String objectToJsonPretty(Object obj) {
        String retStr = "";
        if (obj == null) {
            return retStr;
        }
        try {
            retStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Object to json string failed!", e);
        }
        return retStr;
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

}
