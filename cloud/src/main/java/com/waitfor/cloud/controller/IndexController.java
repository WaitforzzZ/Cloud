package com.waitfor.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author Waitfor
 * @Date 2020-9-21 下午 3:22
 * @Version 1.0
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }
    /**
     * 首页跳转
     */
    @GetMapping("/index")
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }

    /**
     * 404跳转
     */
    @GetMapping("/error")
    public ModelAndView error(ModelAndView mv) {
        mv.setViewName("404");
        return mv;
    }

    /**
     * 管理员页面跳转
     */
    @GetMapping("/admin/index")
    public ModelAndView adminIndex(ModelAndView mv, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //取出用戶名
        String userName = (String)session.getAttribute("userName");
        mv.addObject("userName",userName);
        mv.setViewName("admin/index");
        return mv;
    }

    /**
     * 普通用户页面跳转
     */
    @GetMapping("/user/index")
    public ModelAndView userIndex(ModelAndView mv, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //取出用戶名
        String userName = (String)session.getAttribute("userName");
        mv.addObject("userName",userName);
        mv.setViewName("user/index");
        return mv;
    }

    /**
     * 用户列表
     */
    @GetMapping("/admin/userManager")
    public ModelAndView userManage(ModelAndView mv) {
        mv.setViewName("/admin/userManager");
        return mv;
    }
}
