package com.zyc.crm.workbench.web.service;

import com.zyc.crm.workbench.web.pojo.ActivityRemark;

import java.util.List;

/**
 * 市场活动详情的备注业务
 * @author zyc
 * @version 1.0
 */
public interface ActivityRemarkService {

    /**
     * 根据activityId查询该市场活动下所有备注的明细信息
     * @param activityId
     * @return
     */
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);
}
