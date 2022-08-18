package com.zyc.crm.workbench.web.service;

import com.github.pagehelper.PageInfo;
import com.zyc.crm.workbench.web.pojo.Activity;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据条件分页查询市场活动的列表
     * @param map
     * @return
     */
    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动的总条数
     * @param map
     * @return
     */
    int queryCountOfActivityByCondition(Map<String,Object> map);

    /**
     * 根据ids批量删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询市场活动的信息
     * @param id
     * @return
     */
    Activity queryActivityById(String id);

    /**
     * 保存修改的市场活动
     * @param activity
     * @return
     */
    int saveEditActivity(Activity activity);

    /**
     *查询所有的市场活动
     * @return
     */
    List<Activity> queryAllActivity();

    /**
     * 根据ids批量查询市场活动
     * @param ids
     * @return
     */
    List<Activity> queryActivityByIds(String[] ids);
}
