package com.zyc.crm.workbench.web.service.impl;

import com.zyc.crm.workbench.web.mapper.ActivityMapper;
import com.zyc.crm.workbench.web.pojo.Activity;
import com.zyc.crm.workbench.web.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyc
 * @version 1.0
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity) {
        return this.activityMapper.insertActivity(activity);
    }
}
