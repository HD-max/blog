package com.xzy.blog.service;

import com.xzy.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
public interface TagService {

    Tag saveTag(Tag type);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();  //blogcontroller中input的新增调用的

    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String ids); //获取到多个id

    Tag updateTag(Long id, Tag type);

    void deleteTag(Long id);
}
