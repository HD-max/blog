package com.xzy.blog.service;

import com.xzy.blog.dao.UserRepository;
import com.xzy.blog.po.User;
import com.xzy.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//接口实现
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {//拿到用户名和密码，去数据库查记录，查到了返回一个用户名，失败返回空
        User user= userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;


    }
}
