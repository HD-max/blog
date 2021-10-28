package com.xzy.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录过滤器，相当于一张网。过滤掉除了管理员以外的所有非法用户
public class LoginInterceptor extends HandlerInterceptorAdapter {

    //重写预处理，在登录还未到达前，进行处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("user") == null){//如果未登录，即session中无值获取不到user
            response.sendRedirect("/admin"); //重定向到登录页面
            return false;
        }
        return true; //如果已经登录，那就继续执行

    }
}
