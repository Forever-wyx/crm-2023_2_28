package com.xing.crm.workbench.service;

import com.xing.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> queryActivityRemarkForDetailById(String id);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemarkById(ActivityRemark activityRemark);
}
