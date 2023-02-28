package com.xing.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchIndexController {

    /**
     * 跳转页面
     * @return
     */
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }


}
