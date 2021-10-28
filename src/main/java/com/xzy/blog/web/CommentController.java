package com.xzy.blog.web;

import com.xzy.blog.po.Comment;
import com.xzy.blog.po.User;
import com.xzy.blog.service.BlogService;
import com.xzy.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {  //根据blogid来获取评论列表
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));  //前台输出评论列表
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {  //点击提交以后发布了评论，post方法是接收发布的评论.session-获取用户
        Long blogId = comment.getBlog().getId();   //获取blog.id对象
        comment.setBlog(blogService.getBlog(blogId));  //根据blog.id查询到service
        User user = (User) session.getAttribute("user"); //从session里面获取user，强转成user对象
        if (user != null) {  //判断是否为管理员
            comment.setAvatar(user.getAvatar());  //头像设置为管理员头像
            comment.setAdminComment(true);  //标记为管理员信息
        } else {
            comment.setAvatar(avatar);  //访客
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }



}
