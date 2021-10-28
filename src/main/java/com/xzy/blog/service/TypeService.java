package com.xzy.blog.service;

import com.xzy.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type); //保存，即新增完以后返回一个type

    Type getType(Long id);  //查询，根据id查询type

    Type getTypeByName(String name);  //通过名称查询type

    Page<Type> listType(Pageable pageable);  //分页查询，返回page

    List<Type> listType();  //获取所有的type

    List<Type> listTypeTop(Integer size);

    Type updateType(Long id,Type type);  //修改

    void deleteType(Long id);  //删除
}
