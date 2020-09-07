package com.scosyf.dubbo.web.core.filter;

import com.scosyf.dubbo.web.util.request.HttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HttpRequestFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            LOGGER.info(">>> HttpRequestFilter doFilter");
            ServletRequest bufferedRequest = new HttpRequestWrapper((HttpServletRequest) request);
            chain.doFilter(bufferedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
