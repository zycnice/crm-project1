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


    /**
     * 保存创建的市场活动备注
     * @param remark
     * @return
     */
    int saveCreateActivityRemark(ActivityRemark remark);

    /**
     * 根据id删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 保存修改的市场活动备注
     * @param remark
     * @return
     */
    int saveEditActivityRemark(ActivityRemark remark);
}
