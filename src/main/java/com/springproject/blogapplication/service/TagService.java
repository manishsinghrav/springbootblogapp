package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Tag;

import java.util.List;

public interface TagService {
    public void saveTag(String name);
    public List<Tag>getAllTags();
}
