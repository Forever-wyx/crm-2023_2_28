package com.xing.crm.settings.service;


import com.xing.crm.settings.domain.User;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    /**
     * 根据账号和密码查询用户
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @return
     */
    Object queryUserByLoginActAndPwd(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response);

    /**
     * 安全退出
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询所有用户名
     * @param request
     */
   void queryUserAll(HttpServletRequest request);
}
