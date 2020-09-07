package com.scosyf.dubbo.web.core.config;

import com.scosyf.dubbo.web.core.filter.HttpRequestFilter;
import com.scosyf.dubbo.web.core.interceptor.AppTokenInterceptor;
import com.scosyf.dubbo.web.core.interceptor.OpenTokenInterceptor;
import com.scosyf.dubbo.web.core.interceptor.WebTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private WebTokenInterceptor webTokenInterceptor;

    @Autowired
    private AppTokenInterceptor appTokenInterceptor;

    @Autowired
    private OpenTokenInterceptor openTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webTokenInterceptor).addPathPatterns("/project/api/web/**");
        registry.addInterceptor(appTokenInterceptor).addPathPatterns("/project/api/app/**");
        registry.addInterceptor(openTokenInterceptor).addPathPatterns("/project/api/open/**");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setName("httpRequestFilter");
        filterFilterRegistrationBean.setFilter(new HttpRequestFilter());
        return filterFilterRegistrationBean;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        //文件上传大小设置
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.parse("2MB"));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("2MB"));
        return factory.createMultipartConfig();
    }
}
