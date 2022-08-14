package com.zyc.crm.workbench.web.service;

import com.zyc.crm.workbench.web.pojo.Activity;

/**
 * 市场活动业务
 * @author zyc
 * @version 1.0
 */
public interface ActivityService {

    /**
     * 创建保存市场活动
     * @param activity
     * @return
     */
    int saveCreateActivity(Activity activity);
}
