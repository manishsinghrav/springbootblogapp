package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Tag;
import com.springproject.blogapplication.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void saveTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

}
