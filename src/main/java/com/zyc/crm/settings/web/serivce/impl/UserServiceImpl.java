package com.zyc.crm.settings.web.serivce.impl;


import com.zyc.crm.settings.web.mapper.UserMapper;
import com.zyc.crm.settings.web.pojo.User;
import com.zyc.crm.settings.web.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zyc
 * @version 1.0
 */
//默认也是小写
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return this.userMapper.selectAllUsers();
    }
}
