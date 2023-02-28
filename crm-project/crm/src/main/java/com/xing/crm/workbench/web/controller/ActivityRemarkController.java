package com.xing.crm.workbench.web.controller;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.User;
import com.xing.crm.workbench.domain.ActivityRemark;
import com.xing.crm.workbench.service.impl.ActivityRemarkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkServiceImpl activityRemarkService;

    @Autowired
    private ReturnObject returnObject;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark activityRemark){
        //封装参数,只有activityId、noteContent、createBy是前台传过来的
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        activityRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_YES_EDITED);
        int i = activityRemarkService.insertActivityRemark(activityRemark);
        try{
            if (i > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
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

    @RequestMapping("/workbench/Activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        //根据id删除一条数据
        try{
            int ret = activityRemarkService.deleteActivityRemarkById(id);
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

    @RequestMapping("/workbench/Activity/editActivityRemarkById.do")
    @ResponseBody
    public Object editActivityRemarkById(ActivityRemark activityRemark, HttpSession session){
        activityRemark.setEditTime(FormatDateUtils.formatDateTime(new Date()));
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO_EDITED);
        try {
            int ret = activityRemarkService.updateActivityRemarkById(activityRemark);
            if (ret > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
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
}
