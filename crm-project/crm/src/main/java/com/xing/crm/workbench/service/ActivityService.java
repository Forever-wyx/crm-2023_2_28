package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.Activity;
import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ActivityService {

    Object saveCreateActivity(Activity activity,HttpSession httpSession);

    Object deleteActivityByIds(String[] ids);

    List<Activity> queryAllActivities(HttpServletResponse response) throws Exception;

    List<Activity> queryActivitiesByIds(String[] ids,HttpServletResponse response) throws Exception;

    Object saveCreateActivityByList(MultipartFile multipartFile,HttpSession session);

    Activity queryActivityById(String id);

    Object updateActivityById(Activity activity,HttpSession session);

    Activity queryActivityForDetailById(String id);

    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityForDetailByNameClueId(Map map);

    List<Activity> queryActivityForDetailByIds(String[] ids);

    List<Activity> queryActivityForCovertByNameClueId(Map map);
}
