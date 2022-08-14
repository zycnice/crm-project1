package com.zyc.crm.settings.web.serivce;

import com.zyc.crm.settings.web.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author zyc
 * @version 1.0
 */
public interface UserService {

    //deng
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    //查询所有的用户
    List<User> queryAllUsers();
}
