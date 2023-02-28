package com.xing.crm.workbench.web.controller;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.User;
import com.xing.crm.settings.service.impl.UserServiceImpl;
import com.xing.crm.workbench.domain.Customer;
import com.xing.crm.workbench.mapper.CustomerMapper;
import com.xing.crm.workbench.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ReturnObject returnObject;

    @RequestMapping("/workbench/customer/toIndex.do")
    public String toIndex(HttpServletRequest request){
        //将用户列表保存的request作用域中
        userService.queryUserAll(request);
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/saveCustomer.do")
    @ResponseBody
    public Object saveCustomer(Customer customer, HttpSession session){
        //处理参数
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        customer.setCreateBy(user.getId());
        customer.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        customer.setId(UUIDUtils.getUUID());

        int ret = customerService.saveCustomer(customer);
        try{
            if (ret > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Constants.RETURN_OBJECT_MESSAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_OBJECT_MESSAGE);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/customer/toDetail.do")
    public String toDetail(){
        return "workbench/customer/detail";
    }

    /**
     * 返回用户名，为自动不全功能提供数据支持
     * @param name
     * @return
     */
    @RequestMapping("/workbench/customer/queryAllCustomerName.do")
    @ResponseBody
    public Object queryAllCustomerName(String name){
        List<String> nameList = customerService.queryAllCustomerName(name);
        return nameList;
    }

}
