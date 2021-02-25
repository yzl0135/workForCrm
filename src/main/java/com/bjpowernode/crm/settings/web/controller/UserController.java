package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");

        String path = request.getServletPath();
        System.out.println(path);
        if ("/settings/user/login.do".equals(path)) {
//            xxx(request,response);
            login(request,response);
        } else if ("/settings/user/xxx.do".equals(path)) {
//            xxx(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换成md5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("------------ip:"+ip);

        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user=service.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //如果程序执行到此处表示业务层没有为controller抛出任何异常
            //登录成功
            PrintJson.printJsonFlag(response,true);
        } catch (Exception e) {
            e.printStackTrace();
            String msg=e.getMessage();

            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }

    }
}
