package com.zyc.crm.workbench.web.service.impl;

import com.zyc.crm.workbench.web.mapper.ActivityRemarkMapper;
import com.zyc.crm.workbench.web.pojo.ActivityRemark;
import com.zyc.crm.workbench.web.service.ActivityRemarkService;
import com.zyc.crm.workbench.web.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyc
 * @version 1.0
 */
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService{
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId) {
        return this.activityRemarkMapper.selectActivityRemarkForDetailByActivityId(activityId);
    }
}
