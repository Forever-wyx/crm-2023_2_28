package com.xing.crm.settings.service.impl;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.settings.mapper.UserMapper;
import com.xing.crm.settings.domain.User;
import com.xing.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    //service -> mapper 调用持久层
    @Autowired
    private UserMapper userMapper;

    public Object queryUserByLoginActAndPwd(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response) {
        //封装数据
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //查询用户
        User user = userMapper.selectUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if (user == null){
            //登录失败，用户名或密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else {
            //查询到用户,验证用户是否过期、ip是否受限、用户是否被锁定
            String nowTime = FormatDateUtils.formatDate(new Date());
            if(nowTime.compareTo(user.getExpireTime()) > 0){   //比较时间大小
                //用户过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("用户过期");
            }else if (request.getRemoteHost().contains(user.getAllowIps())){
                //ip受限
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else if(user.getLockState().equals("0")){
                //用户被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("用户被锁定");
            }else {
                //登录成功,则将用户保存到session中,以供后面的页面使用该用户里面的数据
                HttpSession session = request.getSession();
                session.setAttribute(Constants.SESSION_USER,user);
                //登录成功,可以返回一个令牌,然后保存到redis中,做是否登录的验证;后续自己优化
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);

                //实现10内记住密码
                Cookie c1 = new Cookie("loginAct", loginAct);
                Cookie c2 = new Cookie("loginPwd", loginPwd);
                if ("true".equals(isRemPwd)){
                    //选中记住密码
                    c1.setMaxAge(10*24*60*60);
                    c2.setMaxAge(10*24*60*60);
                }else {
                    //没有选中记住密码
                    c1.setMaxAge(0);
                    c2.setMaxAge(0);
                }
                response.addCookie(c1);
                response.addCookie(c2);
            }
        }
        return returnObject;
    }

    /**
     * 安全退出
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        //清除cookies
        Cookie c1 = new Cookie("loginAct", "1");
        Cookie c2 = new Cookie("loginPwd", "1");
        c1.setMaxAge(0);
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);
        //清除session
        HttpSession session = request.getSession();
        session.invalidate();
    }

    /**
     * 查询所有用户，并且将所用用户保存到request中
     * @param request
     */
    public void queryUserAll(HttpServletRequest request) {
        //这里应该验证一些用户是否过期，ip是否被锁定，mapper中已经验证了用户是否被锁定
        //后续自己再优化,代码跟登录验证的差不多
        List<User> userList = userMapper.selectUserAll();
        request.setAttribute("userList",userList);
    }
}
