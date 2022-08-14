package com.zyc.crm.workbench.web.controller;

import com.zyc.crm.commons.constant.Constant;
import com.zyc.crm.commons.pojo.ReturnObject;
import com.zyc.crm.commons.utils.DateUtils;
import com.zyc.crm.commons.utils.UUIDUtils;
import com.zyc.crm.settings.web.pojo.User;
import com.zyc.crm.settings.web.serivce.UserService;
import com.zyc.crm.workbench.web.pojo.Activity;
import com.zyc.crm.workbench.web.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 市场活动控制页面
 * @author zyc
 * @version 1.0
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询所有的用户
        List<User> userList = userService.queryAllUsers();
        //把数据保存到request中，因为是请求查询一次就可以，用request，也可以用model ModelAndView
        request.setAttribute("userList",userList);
        //跳转到市场活动页面activity index
        return "workbench/activity/index";
    }

    //保存创建市场活动
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody //解析成json
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        //封装参数
        //uuid随机生成id
        activity.setId(UUIDUtils.getUUID());
        //创建活动的当前日期
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        //创建用户，谁登录的就谁创建，通过session知道谁是登录的用户id
        activity.setCreateBy(user.getId());

        //封装成json对象
        ReturnObject returnObject = new ReturnObject();
        //写数据要考虑报不报异常，查数据只考虑查不查的出数据
        try {
            //调用service层方法，保存创建的市场活动
            int ret = activityService.saveCreateActivity(activity);

            if (ret > 0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。");
        }
        
        //返回json对象
        return returnObject;
    }
}
