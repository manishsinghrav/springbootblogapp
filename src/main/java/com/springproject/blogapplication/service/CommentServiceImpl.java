package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Comment;
import com.springproject.blogapplication.repository.CommentRepository;
import com.springproject.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Comment> getCommentByPostId(Integer id) {
        return commentRepository.getCommentByPostId(id);
    }

    @Override
    public void saveComment(Comment comment) {
        Timestamp instant = Timestamp.from(Instant.now());
        comment.setCreatedAt(instant);
        comment.setUpdatedAt(instant);
        commentRepository.insertComment(comment.getPostId(), comment.getName(), comment.getEmail(),
                comment.getComment(), comment.getCreatedAt(), comment.getUpdatedAt());
    }

    @Override
    public void deleteCommentByPostId(Integer postId) {
        commentRepository.deleteComment(postId);
    }

    @Override
    public void deleteCommentById(Integer id) {
        commentRepository.deleteCommentUsingId(id);
    }

    @Override
    public Comment getCommentById(Integer id) {
        Optional<Comment> optional = commentRepository.findById(id);

        Comment comment = null;

        if (optional.isPresent()) {
            comment = optional.get();
        } else {
            throw new RuntimeException("not present id= " + id);
        }

        return comment;
    }

    @Override
    public Integer getPostIdFromComments(Integer commentId) {
        return commentRepository.getPostIdUsingCommentId(commentId);
    }

    @Override
    public void updateCommentById(String name, String email, String comment, Integer commentId) {
        Timestamp instant = Timestamp.from(Instant.now());
        commentRepository.updateCommentByCommentId(name, email, comment, instant, commentId);
    }

}
