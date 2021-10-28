package com.xzy.blog.service;

import com.xzy.blog.dao.TypeRepository;
import com.xzy.blog.po.Type;
import com.xzy.blog.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRepository typeRepository;  //注入typeRepository

    @Transactional  //事务
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }  //保存

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    } //查询，根据id查询type对象（findOne没有了）

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    } //分页查询

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {  //更新
        Type t = typeRepository.findById(id).get();  //获取到id
        if (t == null){
            throw new NotFoundException("不存在该类型"); //抛出异常
        }
        BeanUtils.copyProperties(type,t); //把type里的东西copy到t里
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    } //删除
}
