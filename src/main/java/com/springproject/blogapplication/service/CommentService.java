package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> getCommentByPostId(Integer id);

    public void saveComment(Comment comment);

    public void deleteCommentByPostId(Integer postId);

    public void deleteCommentById(Integer id);

    public Comment getCommentById(Integer id);

    public Integer getPostIdFromComments(Integer commentId);

    void updateCommentById(String name, String email, String comment, Integer commentId);

}
