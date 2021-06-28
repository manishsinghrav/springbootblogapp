package com.springproject.blogapplication.controller;

import com.springproject.blogapplication.model.Comment;
import com.springproject.blogapplication.model.Post;
import com.springproject.blogapplication.model.Tag;
import com.springproject.blogapplication.model.User;
import com.springproject.blogapplication.service.CommentService;
import com.springproject.blogapplication.service.PostService;
import com.springproject.blogapplication.service.TagService;

import com.springproject.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private UserService userService;

    @GetMapping("/save/{id}")
    public String commentForm(@PathVariable("id") Integer id,
                              @ModelAttribute("comMent") Comment comment, @ModelAttribute("tag") Tag tag) {
        comment.setPostId(id);
        commentService.saveComment(comment);

        return "redirect:/blog/" + id;
    }

    @PostMapping("/user/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId, Authentication authentication) {
        Integer postId = commentService.getPostIdFromComments(commentId);
        User user = userService.getUserByEmail(authentication.getName());

        Post post=postService.getPostById(postId);

        if(post.getAuthor().equals(user.getUserName())) {

            commentService.deleteCommentById(commentId);
        }
        return "redirect:/blog/" + postId;
    }

    @GetMapping("/user/updateCommentForm/{commentId}")
    public String updateCommentForm(@PathVariable("commentId") Integer commentId, Model model,  Authentication authentication) {
        Comment comment = commentService.getCommentById(commentId);
        Integer postId = commentService.getPostIdFromComments(commentId);
        Post post=postService.getPostById(postId);
        User user = userService.getUserByEmail(authentication.getName());

        if(post.getAuthor().equals(user.getUserName())) {

            model.addAttribute("comment", comment);
            return "commentForm";
        }
        return "redirect:/blog/"+postId;
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
