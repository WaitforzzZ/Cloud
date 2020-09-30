package com.waitfor.cloud.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Waitfor
 * @Date 2020-9-23 上午 9:41
 * @Version 1.0
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("执行了LoginInterceptor的preHandle方法");
        try {
            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
            String userName= (String)request.getSession().getAttribute("userName");
            if(userName != null){
                return true;
            }
            response.sendRedirect(request.getContextPath()+"/index");
        } catch (IOException e) {
            log.info("preHandle方法报错");
        }
        return true;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }
}
