package com.xzy.blog.web.admin;

import com.xzy.blog.po.Type;
import com.xzy.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;
    //size-分页大小,sort-根据id排序，direction-排序方向（正序、倒序）
    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model){
        //查询到的数据放到前端展示
        model.addAttribute("page",typeService.listType(pageable)); //typeService.listType(pageable)——springboot中准备好的获取分页的方式
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){  //返回一个新增页面
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){  //修改
        model.addAttribute("type",typeService.getType(id)); //修改的type会通过model返回到页面上去
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){  //提交。新增
        Type type1 = typeService.getTypeByName(type.getName());  //先获取数据库里的type
        if(type1 != null){  //已经存在新增的type的name
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()){  //Valid代表要校验type，result接收校验结果
            return "admin/types-input";
        }
       Type t = typeService.saveType(type);  //保存完以后，返回一个新的对象
       if(t == null){
            attributes.addFlashAttribute("message","新增失败"); //跳出提示，没有保存成功
       }else {
            attributes.addFlashAttribute("message","新增成功");  //跳出提示，保存成功
       }
       return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){ //保存完以后的修改
        Type type1 = typeService.getTypeByName(type.getName());
        if(type1 != null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.updateType(id,type);
        if(t == null){
            attributes.addFlashAttribute("message","更新失败"); //跳出提示，没有保存成功
        }else {
            attributes.addFlashAttribute("message","更新成功");  //跳出提示，保存成功
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){  //删除
        typeService.deleteType(id);  //根据id去删除
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
