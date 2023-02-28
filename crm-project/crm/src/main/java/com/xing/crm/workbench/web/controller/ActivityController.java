package com.xing.crm.workbench.web.controller;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.settings.service.impl.UserServiceImpl;
import com.xing.crm.workbench.domain.Activity;
import com.xing.crm.workbench.domain.ActivityRemark;
import com.xing.crm.workbench.service.impl.ActivityRemarkServiceImpl;
import com.xing.crm.workbench.service.impl.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private ActivityRemarkServiceImpl activityRemarkService;

    @Autowired
    private ReturnObject returnObject;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层查询所有用户,并将所用用户保存到request中
        userService.queryUserAll(request);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession httpSession){
        return activityService.saveCreateActivity(activity,httpSession);
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,
                                                  String endDate,Integer pageNo,Integer pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        //封装开始的条数
        map.put("beginNo",(pageNo - 1) * pageSize);
        return activityService.queryActivityByConditionForPage(map);
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        return activityService.deleteActivityByIds(id);
    }

    @RequestMapping("/workbench/activity/queryAllActivities.do")
    @ResponseBody
    public void queryAllActivities(HttpServletResponse response) throws Exception{
        activityService.queryAllActivities(response);
    }

    @RequestMapping("/workbench/activity/queryActivitiesByIds.do")
    @ResponseBody
    public void queryActivitiesByIds(String[] id, HttpServletResponse response) throws Exception{
        activityService.queryActivitiesByIds(id,response);
    }

    /**
     * 导入文件
     * @param file
     * @return
     */
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile file,HttpSession session){
        return activityService.saveCreateActivityByList(file,session);
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Activity queryActivityById(String id){
        return activityService.queryActivityById(id);
    }

    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session){
        System.out.println(activity.toString());
        return activityService.updateActivityById(activity,session);
    }

    /**
     * 跳转到 detail 页面,并且将查询出来的数据保存到作用域中
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/toDetailActivity.do")
    public String toDetailActivity(String id,HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkForDetailById(activity.getId());
        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarkList",activityRemarkList);
        return "workbench/activity/detail";
    }
}
