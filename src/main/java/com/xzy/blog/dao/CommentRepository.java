package com.xzy.blog.dao;


import com.xzy.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>{

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);  //根据评论的创建时间，通过sort来排序显示（拿到父类为空的评论）
}
