package com.xing.crm.workbench.web.controller;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.settings.domain.DicValue;
import com.xing.crm.settings.domain.User;
import com.xing.crm.settings.service.UserService;
import com.xing.crm.settings.service.impl.DicValueServiceImpl;
import com.xing.crm.settings.service.impl.UserServiceImpl;
import com.xing.crm.workbench.domain.Tran;
import com.xing.crm.workbench.domain.TranHistory;
import com.xing.crm.workbench.service.impl.CustomerServiceImpl;
import com.xing.crm.workbench.service.impl.TranHistoryServiceImpl;
import com.xing.crm.workbench.service.impl.TranServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TransactionController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DicValueServiceImpl dicValueService;

    @Autowired
    private TranServiceImpl tranService;

    @Autowired
    private ReturnObject returnObject;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private TranHistoryServiceImpl tranHistoryService;

    @RequestMapping("/workbench/transaction/toIndex.do")
    public String toIndex(){
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        userService.queryUserAll(request);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute("stageList",stageList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/save";
    }


    /**
     * 保存tran成功，返回json
     * @param map
     * @return
     */
    @RequestMapping("/workbench/transaction/saveTran.do")
    @ResponseBody
    public Object saveTran(@RequestParam Map<String,Object> map, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        map.put("user",user);
        try{
            tranService.saveTran(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_OBJECT_MESSAGE);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/possibilityByStageName.do")
    @ResponseBody
    public Object possibilityByStageName(String name){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(name);
        return possibility;
    }

    /**
     * 跳转到交易详情页面
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/workbench/transaction/toDetail.do")
    public String toDetail(HttpServletRequest request,String id){
        //根据id查找交易
        Tran tran = tranService.queryTranById(id);
        //根据tranId查找交易历史记录
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryByTranId(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("tran",tran);
        request.setAttribute("tranHistoryList",tranHistoryList);
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";
    }


}
