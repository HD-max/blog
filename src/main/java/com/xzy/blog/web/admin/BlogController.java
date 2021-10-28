package com.xzy.blog.web.admin;

import com.xzy.blog.po.Blog;
import com.xzy.blog.po.User;
import com.xzy.blog.service.BlogService;
import com.xzy.blog.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


//博客管理。即登录拦截器
@Controller
@RequestMapping("/admin")  //全局映射
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRESCT_LIST = "redirect:/admin/blogs";

    private final BlogService blogService;
    private final TypeService typeService;
    private final TagService tagService;

    @Autowired
    public BlogController(BlogService blogService, TypeService typeService, TagService tagService) {
        this.blogService = blogService;
        this.typeService = typeService;
        this.tagService = tagService;
    }

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) { //显示(一页显示几个，排序——更新时间排序，倒序排序)
        model.addAttribute("types", typeService.listType());  //初始化分类
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model){   //查询
        model.addAttribute("page",blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList"; //返回blogs页面下面的blogList片段
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/input")
    public String input(Model model){ //新增
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());  //跳转到新增页面的时候先初始化一个blog，初始化的目的：和修改共用一个页面的时候去返回一个值的时候报错
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){  //修改
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);  //拿到id
        blog.init();  //初始化——把tagid转化成字符串
        model.addAttribute("blog",blog);
        return INPUT;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){ //新增和修改（共用一个方法）时候的提交
        //前端提交的时候除了用户，还有type和tag要提交
        blog.setUser((User) session.getAttribute("user")); //拿到当前的登录用户
        blog.setType(typeService.getType(blog.getType().getId()));  //blog-inputs中“分类”的type.id会自动把属性传到当前的blog,new一个type然后type里面set一个id
        blog.setTags(tagService.listTag(blog.getTagIds()));  //与上面一样，拿到blog-inputs中的tagIds（是1,2,3的字符串）——体现在TagService

        Blog b;
        //更新博客内容以后可以更新view和createtime,解决了BlogServiceIpl中saveblog中view的方法
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);  //新增或修改数据
        }else {
            b = blogService.updateBlog(blog.getId(),blog);  //更新数据
        }

        if(b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");  //跳出提示
        }
        return REDIRESCT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){  //删除
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");  //跳出提示，保存成功
        return REDIRESCT_LIST;
    }
}
