package com.xing.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    //跳转到首页
    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
