package com.wn.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 * @BelongsProject: mall_proj
 * @BelongsPackage: com.wn.interceptor
 * @Author: 廖刚
 * @CreateTime: 2020-05-15 11:33
 * @Description:
 */
@Configuration
public class proWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ManagerInterceptor managerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(managerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/manager/user/login","/user/login.do","/product/**","/order/alipay_callback.do");

    }
}
