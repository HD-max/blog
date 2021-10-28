package com.xzy.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
//springboot aop来进行处理，可以拦截一个切面的方式，把web控制器整个前端请求到业务处理到最终返回，像一个流状过来。aop向切面，对切面前后进行日志处理
//*  请求 url
//*  访问者 ip
//*  调用方法 classMethod
//*  参数 args
//*  返回内容
@Aspect //切面操作
@Component //组件扫码，springboot通过这个注解扫描到组件
public class LogAspect {
    //拿到日志，日志记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //通过注解Pointcut形成一个切面，定义拦截web下的所有的类
    @Pointcut("execution(* com.xzy.blog.web.*.*(..))")
    public void log() {
    }

    //log（）方法在切面之前执行
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //通过attributes方法来拿到HttpServletRequest
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //后面的是stringbuffer类型所以需要tostring一下
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();//类名+方法名
        Object[] args = joinPoint.getArgs(); //拿到请求的参数
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args); //构造requestLog对象
        logger.info("Request : {}",requestLog); //输出requestLog——记录的请求
    }

    @After("log()")
    public void doAfter() {
        //logger.info("----doAfter----");
    }

    //返回的结果，拦截捕获方法的返回内容
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterRuturn(Object result){
        logger.info("Result : {}", result);
    }

    //自定义内部类
    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
