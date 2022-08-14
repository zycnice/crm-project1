package com.zyc.crm.settings.web.interceptor;

import com.zyc.crm.commons.constant.Constant;
import com.zyc.crm.settings.web.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zyc
 * @version 1.0
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return请求前处理
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果用户没有登录成功，则跳转到登录页面
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        if (user == null){
            //判断用户是否为空，空就是没有登录，重定向跳转到首页
            //手动重定向，url必须加项目的名称request.getContextPath()获取项目的名称
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     * 请求后处理
     */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     * 响应和接收
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
