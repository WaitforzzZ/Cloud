package com.waitfor.cloud.configuration;

import com.waitfor.cloud.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Waitfor
 * @Date 2020-9-23 上午 10:01
 * @Version 1.0
 */
//@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        registration.addPathPatterns("/**");                      //所有路径都被拦截
        registration.excludePathPatterns(                         //添加不拦截路径
                "/",            //首页
                "/index",            //登录
                "/resource/**"             //静态资源
        );
    }
}
