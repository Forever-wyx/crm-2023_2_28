package com.xing.crm.workbench.web.controller;

import com.xing.crm.commons.domain.ReturnObject;
import com.xing.crm.commons.utils.Constants;
import com.xing.crm.commons.utils.FormatDateUtils;
import com.xing.crm.commons.utils.UUIDUtils;
import com.xing.crm.settings.domain.DicValue;
import com.xing.crm.settings.domain.User;
import com.xing.crm.settings.mapper.UserMapper;
import com.xing.crm.workbench.domain.Activity;
import com.xing.crm.workbench.domain.Clue;
import com.xing.crm.workbench.domain.ClueActivityRelation;
import com.xing.crm.workbench.domain.ClueRemark;
import com.xing.crm.workbench.service.impl.ActivityServiceImpl;
import com.xing.crm.workbench.service.impl.ClueActivityRelationServiceImpl;
import com.xing.crm.workbench.service.impl.ClueRemarkServiceImpl;
import com.xing.crm.workbench.service.impl.ClueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private ClueServiceImpl clueService;

    @Autowired
    private ClueRemarkServiceImpl clueRemarkService;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReturnObject returnObject;

    @Autowired
    private ClueActivityRelationServiceImpl clueActivityRelationService;

    @RequestMapping("/workbench/clue/toIndex.do")
    public String toIndex(HttpServletRequest request){
        List<User> userList = userMapper.selectUserAll();
        List<DicValue> appellationList = clueService.queryDicValueByTypeCode("appellation");
        List<DicValue> sourceList = clueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = clueService.queryDicValueByTypeCode("stage");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        //封装参数
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        clue.setCreateBy(user.getId());
        clue.setCreateTime(FormatDateUtils.formatDateTime(new Date()));
        clue.setId(UUIDUtils.getUUID());
        try{
            int ret = clueService.insertClue(clue);
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

    @RequestMapping("/workbench/clue/toClueDetail.do")
    public String toClueDetail(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBound.do")
    @ResponseBody
    public Object saveBound(String[] activityId,String clueId){
        ArrayList<ClueActivityRelation> clueActivityRelationList = new ArrayList<ClueActivityRelation>();
        for (String aid : activityId) {
            //list集合里面存的是对象的地址，所以每次都需要创建一个新对象
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(aid);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelationList.add(clueActivityRelation);
        }

        try {
            int ret = clueActivityRelationService.insertClueActivityRelationService(clueActivityRelationList);
            if (ret > 0){
                //关联成功才调用;这里需要返回这次关联的市场活动,
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
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

    @RequestMapping("/workbench/clue/saveUnBound.do")
    @ResponseBody
    public Object saveUnBound(ClueActivityRelation relation){
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelation(relation);
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = clueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/searchActivityForCovertByNameClueId.do")
    @ResponseBody
    public Object searchActivityForCovertByNameClueId(String activityName,String clueId){
        //封装参数
        Map map = new HashMap<String,Object>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList = activityService.queryActivityForCovertByNameClueId(map);
        return activityList;
    }


    @RequestMapping("/workbench/clue/saveConvert.do")
    @ResponseBody
    public Object saveConvert(String clueId,String money,String name,String  expectedDate,String stage,String activityId,String isCreateTransaction,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //封装参数
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTransaction",isCreateTransaction);
        map.put(Constants.SESSION_USER,user);

        try{
            clueService.saveCoverClue(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Constants.RETURN_OBJECT_MESSAGE);
        }
        return returnObject;
    }
}
