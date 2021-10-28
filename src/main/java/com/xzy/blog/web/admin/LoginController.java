package com.xzy.blog.web.admin;

import com.xzy.blog.po.User;
import com.xzy.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

//前端web的登陆控制器
@Controller
@RequestMapping("/admin")  //访问admin就会触发return
public class LoginController {

    @Autowired
    private UserService userService;

    //跳转到登陆页面
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    //登陆
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes){
        User user=userService.checkUser(username,password); //拿到用户名和密码
        if(user != null){
            user.setPassword(null);  //不将密码传到前端
            session.setAttribute("user",user); //如果拿到了user，将user存储到session中
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message","用户名和密码错误");  //前端提示(不能用model的办法来提示，因为用的重定向)
            return "redirect:/admin"; //使用重定向返回登陆页面，不要使用（return "admin/login"）虽然可以返回，但是再次登陆会出现路径错误
        }
    }

    //注销登陆
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");  //移除掉session中获取的user
        return "redirect:/admin";
    }
}
