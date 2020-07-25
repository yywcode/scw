package com.atguigu.crowd.mvc.interceptor;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    //使用SpringSecurity之后不用拦截器了
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //1.通过request对象获取session对象
        HttpSession session = httpServletRequest.getSession();

        //2.尝试从Session域中获取Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        //3.判断Admin对象是否为空
        if (admin == null) {

            //4.抛出异常
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_LOGIN_ACCESS_FORBIDEN);
        }

        //5.如果admin对象不为空，则放行
        return true;
    }


}
