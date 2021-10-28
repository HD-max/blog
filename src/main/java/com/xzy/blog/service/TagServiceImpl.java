package com.xzy.blog.service;

import com.xzy.blog.dao.TagRepository;
import com.xzy.blog.po.Tag;
import com.xzy.blog.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }


    @Override
    public List<Tag> listTag(String ids) { //1,2,3（把这种字符串转换成一个集合）
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {  //该方法把字符串转换成数组
        List<Long> list = new ArrayList<>();  //定义一个long类型的数组
        if (!"".equals(ids) && ids != null) { //不等于空，不等于null
            String[] idarray = ids.split(","); //先获取ids，再截取
            for (int i=0; i < idarray.length;i++) { //把截取到的数一个个塞到list中
                list.add(new Long(idarray[i]));  //把字符串转换成数组
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.findById(id).get();
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
