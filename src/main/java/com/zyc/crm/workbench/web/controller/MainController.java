package com.zyc.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 工作台控制层
 * @author zyc
 * @version 1.0
 */

@Controller
public class MainController {

    @RequestMapping("/workbench/main/index.do")
    public String index(){
            //跳转到main index
            return "workbench/main/index";
        }
}
