package com.xzy.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//先定义了错误页面，然后全局处理异常

//统一异常的处理器，通过此类ControllerAdvice拦截所有的Controller注解的
//可以跳转到自己定义的错误页面
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger= LoggerFactory.getLogger(this.getClass()); //获取日志

    @ExceptionHandler(Exception.class) //ExceptionHandler标识这个方法可以做异常处理,统一拦截所有Exception
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {//request能够知道访问的路径有异常
        //记录异常url和异常
        logger.error("Request URL : {}, Exception : {}",request.getRequestURL(),e); //记录异常信息，在控制台输出异常信息

        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) !=null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL()); //获取url
        mv.addObject("exception",e);  //获取异常信息
        mv.setViewName("error/error"); //约定返回到哪个页面
        return mv;
    }
}
