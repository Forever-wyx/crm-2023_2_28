package com.xing.crm.workbench.web.controller;

import com.xing.crm.settings.domain.DicValue;
import com.xing.crm.settings.mapper.DicValueMapper;
import com.xing.crm.settings.service.impl.DicValueServiceImpl;
import com.xing.crm.settings.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ContactsController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DicValueServiceImpl dicValueService;

    @RequestMapping("/workbench/contacts/toIndex.do")
    public String toIndex(HttpServletRequest request){
        //将用户保存到request作用域中
        userService.queryUserAll(request);
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");

        request.setAttribute("sourceList",sourceList);
        request.setAttribute("appellationList",appellationList);
        return "workbench/contacts/index";
    }
}
