package com.xing.crm.workbench.mapper;

import com.xing.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {

    List<ActivityRemark> selectActivityRemarkByActivityId(String ActivityId);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemarkById(ActivityRemark activityRemark);
}