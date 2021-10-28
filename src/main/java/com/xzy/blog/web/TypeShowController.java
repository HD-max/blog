package com.xzy.blog.web;


import com.xzy.blog.po.Type;
import com.xzy.blog.service.BlogService;
import com.xzy.blog.service.TypeService;
import com.xzy.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);  //查询（全表）的分类
        if (id == -1) {   //如果等于-1，相当于是从导航点进来的
           id = types.get(0).getId();  //拿到一个博客分类的id
        }
        BlogQuery blogQuery = new BlogQuery();  //查询对象
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);  //前端显示所有分类
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id); //活跃id，就是选中的id（用于增加teal颜色）
        return "types";
    }
}
