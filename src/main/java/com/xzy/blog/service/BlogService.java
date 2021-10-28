package com.xzy.blog.service;

import com.xzy.blog.po.Blog;
import com.xzy.blog.po.Tag;
import com.xzy.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id); //查询

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);  //分页查询

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable); //专门用来分页查询tagid

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);  //新增

    Blog updateBlog(Long id,Blog blog); //修改

    void deleteBlog(Long id); //删除
}
