package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        System.out.println("进入到了UserServiceImpl执行login方法");
        Map<String,String> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user=userDao.login(map);
        if(user==null){
            throw new LoginException("账号密码错误");
        }

        String expireTime = user.getExpireTime();
        String time = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(time)<0){
            throw new LoginException("账号已过期");
        }

        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已被锁定,请联系管理员");
        }

        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }

        return user;
    }
}
