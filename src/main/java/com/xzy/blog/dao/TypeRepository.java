package com.xzy.blog.dao;

import com.xzy.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {

    Type findByName(String name); //查询到一个type的name

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
