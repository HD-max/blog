package com.xzy.blog.dao;

import com.xzy.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

//使用springboot中的jpa。UserRepository里面继承了增删改查的方法可以直接使用
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);
}
