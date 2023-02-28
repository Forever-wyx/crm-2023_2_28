package com.xing.crm.settings.web.controller;

import com.xing.crm.settings.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    /**
     * 调用service业务层，认证用户
     * 这个方法只做用户认证,没有做登录成功后的页面跳转
     * 在现在这个前后端分离的时代,我个人认为应该在这里做页面跳转,不应该在jsp中做页面跳转
     *      但是用这个做页面跳转又不知道怎么使用异步请求(登录成功跳转页面,失败异步请求返回一个json)
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @param request
     * @return  返回json对象
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response){
        return userService.queryUserByLoginActAndPwd(loginAct,loginPwd,isRemPwd,request,response);
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        //调用业务层,清楚cookie session
        userService.logout(request,response);
        return "redirect:/";
    }
}
