package com.zyc.crm.workbench.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyc.crm.commons.constant.Constant;
import com.zyc.crm.commons.pojo.ReturnObject;
import com.zyc.crm.commons.utils.DateUtils;
import com.zyc.crm.commons.utils.UUIDUtils;
import com.zyc.crm.settings.web.pojo.User;
import com.zyc.crm.settings.web.serivce.UserService;
import com.zyc.crm.workbench.web.pojo.Activity;
import com.zyc.crm.workbench.web.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                        int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        //map中的key值要和mapper中的值保持一致
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        //第几页
        map.put("beginNo",(pageNo-1)*pageSize);
        //每页显示的条数
        map.put("pageSize",pageSize);

        //调用service层方法，查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //根据查询结果，生成响应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id){
        //把封装的对象放到ReturnObject
        ReturnObject returnObject = new ReturnObject();
        //增删改都要看是否报异常，应为对数据进行改变了
        try {
            //调用service层方法，删除市场活动
            int ret = activityService.deleteActivityByIds(id);
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
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        //调用service层方法，查询市场活动
        Activity activity = activityService.queryActivityById(id);
        //根据查询结果，返回响应信息
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        //进一步封装参数
        ReturnObject returnObject = new ReturnObject();
        //通过session知道登录人是谁
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        //修改时间
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        //修改人
        activity.setEditBy(session.getId());
        try {
            //写数据都要考虑异常
            //调用service层方法，保存修改的市场活动
            int ret = activityService.saveEditActivity(activity);
            if (ret > 0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙请重试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/fileDownload.do")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();

        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/excel/studentList.xls");
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()];
        //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=studentList.xls");
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;
    }


    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {

        //调用service层方法，查询所有的市场活动
        List<Activity> activityList = activityService.queryAllActivity();
        //创建excel文件，并且把activityList写入到excel文件中
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //使用wb创建HSSFSheet对象，对应web文件中的一页
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //使用sheet创建HSSFRow对象，对应sheet中的一行
        //行号从0开始，依次增加
        HSSFRow row = sheet.createRow(0);
        //列号从0开始，依次增加
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("所有者");


        cell = row.createCell(2);
        cell.setCellValue("名称");

        cell = row.createCell(3);
        cell.setCellValue("开始日期");

        cell = row.createCell(4);
        cell.setCellValue("结束日期");

        cell = row.createCell(5);
        cell.setCellValue("成本");

        cell = row.createCell(6);
        cell.setCellValue("描述");

        cell = row.createCell(7);
        cell.setCellValue("创建时间");

        cell = row.createCell(8);
        cell.setCellValue("创建者");

        cell = row.createCell(9);
        cell.setCellValue("修改时间");

        cell = row.createCell(10);
        cell.setCellValue("修改者");



        if(activityList != null && activityList.size() > 0){
            //使用sheet创建activityList大小个HSSRow对象
            for (int i = 0; i < activityList.size(); i++) {
                Activity activity = activityList.get(i);
                //行，第0行是表头
                row = sheet.createRow(i + 1);
                //列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());


                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());

                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());

                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());

                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());

                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());

                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());

                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());

            }
        }
        //调用工具函数生成excel文件
        //通过输出流
//        FileOutputStream os = new FileOutputStream("D:\\project\\crm-project\\src\\main\\webapp\\static\\excel\\studentList.xls");
//        wb.write(os);
//        //关闭数据流
//        os.close();
//        wb.close();

        //下载
        response.setContentType("application/octet-stream;charset=UTF-8");
        //浏览器在接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息，即使打不开，也会调用应用程序打开，只用实在打不开，才会激活文件的保存
        //设置响应信息，使浏览器接收到响应信息之后，直接激活文件下载窗口， 即使可以打开也不打开
        response.addHeader("Content-Disposition", "attachment;filename=ActivityList.xls");
        ServletOutputStream out  = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //获取ServletContext对象
//        ServletContext servletContext = session.getServletContext();
//
//        //获取服务器中文件的真实路径
//        String realPath = servletContext.getRealPath("D:\\project\\crm-project\\src\\main\\webapp\\static\\excel\\studentList.xls");
//        //创建输入流
//        InputStream is = new FileInputStream(realPath);
//        //创建字节数组
//        byte[] bytes = new byte[is.available()];
//        //将流读到字节数组中
//        is.read(bytes);
//        //创建HttpHeaders对象设置响应头信息
//        MultiValueMap<String, String> headers = new HttpHeaders();
//        //设置要下载方式以及下载文件的名字
//        headers.add("Content-Disposition", "attachment;filename=studentList.xls");
//        //设置响应状态码
//        HttpStatus statusCode = HttpStatus.OK;
//        //创建ResponseEntity对象
//        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
//        //关闭输入流
//        is.close();
//        return responseEntity;
    }

    @RequestMapping(value = "/workbench/activity/exportAllActivityByIds.do")
    public void exportAllActivityByIds(HttpServletResponse response, String[] id) throws IOException {

        //调用service层方法，查询所有的市场活动
        List<Activity> activityList = activityService.queryActivityByIds(id);
        //创建excel文件，并且把activityList写入到excel文件中
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //使用wb创建HSSFSheet对象，对应web文件中的一页
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //使用sheet创建HSSFRow对象，对应sheet中的一行
        //行号从0开始，依次增加
        HSSFRow row = sheet.createRow(0);
        //列号从0开始，依次增加
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("所有者");


        cell = row.createCell(2);
        cell.setCellValue("名称");

        cell = row.createCell(3);
        cell.setCellValue("开始日期");

        cell = row.createCell(4);
        cell.setCellValue("结束日期");

        cell = row.createCell(5);
        cell.setCellValue("成本");

        cell = row.createCell(6);
        cell.setCellValue("描述");

        cell = row.createCell(7);
        cell.setCellValue("创建时间");

        cell = row.createCell(8);
        cell.setCellValue("创建者");

        cell = row.createCell(9);
        cell.setCellValue("修改时间");

        cell = row.createCell(10);
        cell.setCellValue("修改者");



        if(activityList != null && activityList.size() > 0){
            //使用sheet创建activityList大小个HSSRow对象
            for (int i = 0; i < activityList.size(); i++) {
                Activity activity = activityList.get(i);
                //行，第0行是表头
                row = sheet.createRow(i + 1);
                //列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());


                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());

                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());

                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());

                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());

                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());

                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());

                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());

            }
        }


        //下载
        response.setContentType("application/octet-stream;charset=UTF-8");
        //浏览器在接收到响应信息之后，默认情况下，直接在显示窗口中打开响应信息，即使打不开，也会调用应用程序打开，只用实在打不开，才会激活文件的保存
        //设置响应信息，使浏览器接收到响应信息之后，直接激活文件下载窗口， 即使可以打开也不打开
        //直接内存传内存，不用通过内存-》磁盘-》内存的操作
        response.addHeader("Content-Disposition", "attachment;filename=ActivityList.xls");
        ServletOutputStream out  = null;
        try {
            out = response.getOutputStream();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
