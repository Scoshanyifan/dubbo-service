package com.scosyf.dubbo.web.util.request;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;

/**
 * @project: bucks
 * @author: kunbu
 * @create: 2019-09-09 14:58
 **/
public class HttpRequestUtil {

    public static void printHttpRequest(HttpServletRequest request, Logger logger) {

        logger.info(">>> getRequestURL: {}", request.getRequestURL());
        logger.info(">>> getRequestURI: {}", request.getRequestURI());
        logger.info(">>> getRemoteAddr: {}", request.getRemoteAddr());
        logger.info(">>> getRemoteHost: {}", request.getRemoteHost());
        logger.info(">>> getRemotePort: {}", request.getRemotePort());
        logger.info(">>> getLocalPort: {}", request.getLocalPort());
        logger.info(">>> getServerPort: {}", request.getServerPort());
        logger.info(">>> getMethod: {}", request.getMethod());
        logger.info(">>> getContextPath: {}", request.getContextPath());
        logger.info(">>> getCharacterEncoding: {}", request.getCharacterEncoding());
        logger.info(">>> getServletPath: {}", request.getServletPath());

        logger.info("=== header ===");
        Enumeration<String> headers = request.getHeaderNames();
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                logger.info(">>> header {}: {}", header, request.getHeader(header));
            }
        }

        logger.info("=== param ===");
        Map<String, String[]> paramMap = request.getParameterMap();
        if (paramMap != null && !paramMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                logger.info(">>> param key:{}, value:{}", entry.getKey(), JSONObject.toJSON(entry.getValue()));
            }
        }

        logger.info("=== cookie ===");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                logger.info(">>> cookie, {}: {}", c.getName(), c.getValue());
            }
        }

        logger.info("=== session ===");
        HttpSession httpSession = request.getSession();
        if (httpSession != null) {
            logger.info(">>> session id: {}", httpSession.getId());

            Enumeration<String> attributes = httpSession.getAttributeNames();
            if (attributes != null) {
                while (attributes.hasMoreElements()) {
                    String attribute = attributes.nextElement();
                    logger.info(">>> httpSession attribute, {}: {}", attribute, httpSession.getAttribute(attribute));
                }
            }
        }
    }

    public static byte[] getPostBodyByte(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            return null;
        }

        InputStream is = request.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int index;
        while((index = is.read(buf)) != -1) {
            bos.write(buf, 0, index);
        }
        return bos.toByteArray();
    }

    public static String getPostBodyJson(HttpServletRequest request) throws IOException {
        byte[] data = getPostBodyByte(request);
        if (data != null && data.length > 0) {
            String encode = request.getCharacterEncoding();
            if (encode == null) {
                encode = "utf8";
            }
            return JSONObject.parseObject(new String(data, encode)).toJSONString();
        }
        return null;
    }

    public static String getPostDataPretty(HttpServletRequest request) throws IOException {
        StringBuffer data = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line = null;
        while (null != (line = reader.readLine())) {
            data.append(line);
        }
        return data.toString();
    }

}
