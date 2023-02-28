package com.xing.crm.workbench.service.impl;

import com.xing.crm.workbench.domain.ActivityRemark;
import com.xing.crm.workbench.mapper.ActivityRemarkMapper;
import com.xing.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    public List<ActivityRemark> queryActivityRemarkForDetailById(String id) {
        return activityRemarkMapper.selectActivityRemarkByActivityId(id);
    }

    public int insertActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }

    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }


    public int updateActivityRemarkById(ActivityRemark activityRemark){
        return activityRemarkMapper.updateActivityRemarkById(activityRemark);
    }
}
