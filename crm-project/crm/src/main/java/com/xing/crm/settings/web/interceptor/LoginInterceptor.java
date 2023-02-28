package com.xing.crm.settings.web.interceptor;

import com.xing.crm.commons.utils.Constants;
import com.xing.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //登录拦截;如果用户没有登录->跳转到登录页面
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null){
            //用户没有登录,跳转到登录页面  -->  重定向
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());      //重定向时,urL必须加项目的名字
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
