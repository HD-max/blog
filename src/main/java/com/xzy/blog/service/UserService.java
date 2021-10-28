package com.xzy.blog.service;

import com.xzy.blog.po.User;
//定义用户名和密码检查接口
public interface UserService {

    User checkUser(String username,String password);
}
