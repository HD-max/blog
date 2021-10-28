package com.xzy.blog.interceptor;

//登录过滤器的配置。仅靠LoginInterceptor的登录过滤器不行，因为不知道需要过滤掉哪些内容

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")    //过滤掉（admin/..）这类的所有
                .excludePathPatterns("/admin")      //排除掉不过滤的，因为不排除无法登录（会在登录界面一直转圈)，也无法刷新（会重新提交表单，也会转圈）
                .excludePathPatterns("/admin/login");
        super.addInterceptors(registry);
    }
}
