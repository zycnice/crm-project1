package com.zyc.crm.settings.web.controller;

import com.zyc.crm.commons.constant.Constant;
import com.zyc.crm.commons.pojo.ReturnObject;
import com.zyc.crm.commons.utils.DateUtils;
import com.zyc.crm.settings.web.pojo.User;
import com.zyc.crm.settings.web.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyc
 * @version 1.0
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *url要和controller方法处理完请求之后，响应信息返回的页面的资源目录保持一致
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //请求转发到登录页面
        return "/settings/qx/user/login";
    }

    //登录验证
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    /**
     * 将Java对象直接作为控制器方法的返回值返回，就会自动转换为Json格式的字符串
     *  @RestController注解是springMVC提供的一个复合注解，标识在控制器的类上，就相当于为类添加了
     *  @Controller注解，并且为其中的每个方法添加了@ResponseBody注解
     */
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        //根据查询结构生成响应信息
        //用于返回json数据
        ReturnObject returnObject = new ReturnObject();
        if (user == null){
            //登录失败，用户名或者密码错误
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        }else {//进一步判断账号是否合法
            //做时间格式化
            //提取工具类DateUtils
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String nowStr = sdf.format(new Date());//获取当前时间并格式化
            String nowStr = DateUtils.formatDateTime(new Date());
            if (nowStr.compareTo(user.getExpireTime()) > 0){
                //登录失败，账号有效期已过
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if ("0".equals(user.getLockState())){
                //登录失效，状态被锁定
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                //request.getRemoteAddr()
                //如果字符串中包含只等的字符或字符串时，java.lang.String.contains()方法返回true。
                //用户的id地址受限，登录失败
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip地址被限制");
            }else {
                //登录成功
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);

                //把user保存在session中,把字符提取到成常用常量，这样维护的话，直接修改常量值就行了
                session.setAttribute(Constant.SESSION_USER,user);
//                session.setAttribute("sessionUser",user);

                //如果需要记住密码，则往外写cookie
                //因为传入的isRemPwd是String类型的，check是只有true或false
                if ("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    //响应回页面
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else {
                    //把没有过期的cookie删除
                    Cookie c1 = new Cookie("loginAct", "");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd","");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie
        Cookie c1 = new Cookie("loginAct", "");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","");
        c2.setMaxAge(0);
        response.addCookie(c2);

        //销毁session
        session.invalidate();

        //跳转到首页
        //借助springmvc来重定向，response.sendRedirect("/crm"/);
        return "redirect:/";
    }
}
