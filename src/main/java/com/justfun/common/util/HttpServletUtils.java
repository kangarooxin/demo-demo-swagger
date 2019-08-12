package com.justfun.common.util;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pangxin001
 */
public class HttpServletUtils {

    public static final String SCHEME_HTTP = "http";

    public static final String SCHEME_HTTPS = "https";

    private static final String ENCODE_UTF_8 = "UTF-8";

    /**
     * 获取基本路径
     *
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String scheme = request.getScheme();
        int serverPort = request.getServerPort();
        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if(!(SCHEME_HTTP.equals(scheme) && serverPort == 80) && !((SCHEME_HTTPS.equals(scheme) && serverPort == 443))) {
            sb.append(request.getServerPort());
        }
        sb.append(request.getContextPath());
        return sb.toString();
    }

    /**
     * 取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName;
        while(headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            retMap.put(headerName, request.getHeader(headerName));
        }
        return retMap;
    }

    /**
     * 取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<>();
        Map<String, String[]> paramMap = request.getParameterMap();
        for (String key : paramMap.keySet()) {
            retMap.put(key, request.getParameter(key).trim());
        }
        return retMap;
    }

    /**
     * 取请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getMultipartParameterMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<>();
        if(request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
            MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
            for (String key : multiValueMap.keySet()) {
                retMap.put(key, String.valueOf(getSumFileSize(multiValueMap.get(key))));
            }
        }
        return retMap;
    }

    private static long getSumFileSize(List<MultipartFile> multipartFiles) {
        long size = 0;
        if(multipartFiles != null) {
            for(MultipartFile file : multipartFiles) {
                if(file != null) {
                    size += file.getSize();
                }
            }
        }
        return size;
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getRequestContent(HttpServletRequest request) {
        return getRequestContent(request, ENCODE_UTF_8);
    }

    /**
     *
     * @param request
     * @param charset
     * @return
     */
    public static String getRequestContent(HttpServletRequest request, String charset) {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = request.getInputStream();
            //已HTTP请求输入流建立一个BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
            //读取HTTP请求内容
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                //在页面中显示读取到的请求参数
                sb.append(buffer);
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 是否ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request != null && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * ServletRequest to HttpServletRequest
     *
     * @param request
     * @return
     */
    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest)request;
    }

    /**
     * ServletResponse to HttpServletResponse
     *
     * @param response
     * @return
     */
    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse)response;
    }

    /**
     * 是否GET请求
     *
     * @param request
     * @return
     */
    public static boolean isGet(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(RequestMethod.GET.name());
    }

    /**
     * 是否POST请求
     *
     * @param request
     * @return
     */
    public static boolean isPost(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(RequestMethod.POST.name());
    }

    /**
     * 是否静态资源请求
     *
     * @param request
     * @return
     */
    public static boolean isStatic(HttpServletRequest request) {
        String url = request.getRequestURI().toLowerCase();
        return url.endsWith(".ico")
                || url.endsWith(".css")
                || url.endsWith(".js")
                || url.endsWith(".swf")
                || url.endsWith(".txt")
                || url.endsWith(".jpg")
                || url.endsWith(".png")
                || url.endsWith(".jpeg")
                || url.endsWith(".gif");
    }
}
