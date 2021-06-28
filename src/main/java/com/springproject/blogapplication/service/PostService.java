package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    public List<Post> getAllPost();
    public void savePost(Post post);
    public Post getPostById(Integer id);
    public void deletePost(Integer id);
    public void updatePostByID(Post post, Integer id);
    Page<Post> findPaginated( Integer pageNo, Integer pageSize, String sortingField, String sortingOrder);
    public List<Post> getPostByAuthor(String name);
    public List<Post> getPostByTagName(String tag);
    public List<Post> getPostByPublishedAt(String publishedAt);
    public List<Post> findAllSearchMatchByKeyword( String keyWord);
}
