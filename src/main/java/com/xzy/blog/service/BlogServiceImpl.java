package com.xzy.blog.service;

import com.xzy.blog.dao.BlogRepository;
import com.xzy.blog.po.Blog;
import com.xzy.blog.po.Type;
import com.xzy.blog.util.MarkdownUtils;
import com.xzy.blog.util.MyBeanUtils;
import com.xzy.blog.vo.BlogQuery;
import com.xzy.blog.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {   //博客推荐
        Blog blog = blogRepository.findById(id).get();  //根据id查询
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        //copy blog 给 b
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));  //调用markdown返回html的内容
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {  //动态组合查询
        return blogRepository.findAll(new Specification<Blog>() {  //第一个参数，new出来的，第二个参数是pageable
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();  //把要组合的条件放在list里面
                if(!"".equals(blog.getTitle()) && blog.getTitle() != null){  //如果不为空字符串和空（第一个查询条件是title）
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%")); //构造了like查询的条件,拿到查询对象title属性值的名字
                }
                if (blog.getTypeId() != null){  //第二个查询条件是type的id
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId())); //equal里拿到blog的type对象。拿到blog里面type的id
                }
                if (blog.isRecommend()){  //第三个查询条件是recommend
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));  //查询语句（类似sql语句）
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags"); //当前的blog关联tags对象
                return cb.equal(join.get("id"),tagId);  //返回page对象
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {  //全局搜索相关
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {  //首页的一个展示
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");  //sort排序
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();  //获取年份
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {  //循环
            map.put(year, blogRepository.findByYear(year));   //拿到该年份对应的数据列表
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){  //如果blog id为空，则新增
            blog.getCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0); //浏览次数
        }else {
            blog.setUpdateTime(new Date()); //否则不是新增，修改更新时间
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();   //根据id查询
        if(b == null){  //如果为空，更新对象不存在
            throw new NotFoundException("该博客不存在");
        }
        //通过MyBeanUtils过滤掉了属性值为空的属性，只复制blog中有值的属性到b中
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    } //删除
}
