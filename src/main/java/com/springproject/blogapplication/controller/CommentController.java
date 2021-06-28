package com.springproject.blogapplication.controller;

import com.springproject.blogapplication.model.Comment;
import com.springproject.blogapplication.model.Tag;
import com.springproject.blogapplication.service.CommentService;
import com.springproject.blogapplication.service.PostService;
import com.springproject.blogapplication.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

    @GetMapping("/save/{id}")
    public String commentForm(@PathVariable("id") Integer id,
                              @ModelAttribute("comMent") Comment comment, @ModelAttribute("tag") Tag tag) {
        comment.setPostId(id);
        commentService.saveComment(comment);

        return "redirect:/blog/" + id;
    }

    @PostMapping("/user/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId) {
        Integer postId = commentService.getPostIdFromComments(commentId);
        commentService.deleteCommentById(commentId);

        return "redirect:/blog/" + postId;
    }

    @GetMapping("/user/updateCommentForm/{commentId}")
    public String updateCommentForm(@PathVariable("commentId") Integer commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);

        return "commentForm";
    }

    @PostMapping("/saveUpdate/{commentId}")
    public String updateComment(@PathVariable("commentId") Integer commentId,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "email") String email,
                                @RequestParam(name = "comment") String comment) {
        commentService.updateCommentById(name, email, comment, commentId);
        int postId=commentService.getPostIdFromComments(commentId);
        return "redirect:/blog/"+postId;
    }
}
