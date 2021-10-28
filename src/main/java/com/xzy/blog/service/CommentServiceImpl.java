package com.xzy.blog.service;


import com.xzy.blog.dao.CommentRepository;
import com.xzy.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime"); //按照创建时间排序
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);   //只拿到顶级的comment数据，即父集为空的
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();   //获取当前parentcomment的值。有值为-1（前端blog中给的）
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());  //拿到你要回复的父集（评论），再set到子集（comment）里面
        } else {
            comment.setParentComment(null);  //防止报错。comment本身传了一个-1过来，comment里面new了一个对象但是对象没有持久化，会报错，null了以后就不会了。
        }
        comment.setCreateTime(new Date());  //数据初始化。新建评论的创建时间
        return commentRepository.save(comment);
    }


    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        //拿到list以后，再新建一个commentList集合，把值copy一份到集合里（避免出现对数据库的数据的操作）
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        //拿到list（顶级结点）后，进行循环遍历，拿到ReplyComments（即子集）。再去循环，找到下一代子集
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();  //一个公共容器
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);  //反复调用
                }
            }
        }
    }
}
