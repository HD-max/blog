package com.xzy.blog.service;

import com.xzy.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);   //根据blogid获取评论列表

    Comment saveComment(Comment comment);  //保存评论
}
