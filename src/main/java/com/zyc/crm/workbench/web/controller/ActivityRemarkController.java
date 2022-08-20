package com.zyc.crm.workbench.web.controller;

import com.zyc.crm.workbench.web.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 市场活动的详细页的备注业务
 * @author zyc
 * @version 1.0
 */
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;
}
