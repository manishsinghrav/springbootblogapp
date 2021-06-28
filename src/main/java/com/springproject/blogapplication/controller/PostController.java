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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        return paginatedPage(1, "author", "asc", model);
    }

    @GetMapping("/user/newpost")
    public String newPostForm(Model model, Authentication authentication) {
        Post posts = new Post();
        User user = userService.getUserByEmail(authentication.getName());

        model.addAttribute("caseId", "save");
        model.addAttribute("post", posts);

        if (user.getRole().equals("ROLE_ADMIN")) {
            return "adminpostform";
        }

        return "newform";
    }

    @PostMapping("/savepost")
    public String savePost(@RequestParam("tag") String tags, @ModelAttribute("post") Post post, Authentication authentication) {
        String[] tagsArray = tags.split(",");
        List<String> tagsList = Arrays.asList(tagsArray);
        User user = userService.getUserByEmail(authentication.getName());
        post.setUserName(authentication.getName());
        post.setAuthor(user.getUserName());

        for (String tagStr : tagsList) {
            Tag tag = new Tag();
            tag.setName(tagStr);
            post.getTags().add(tag);
            tag.getPosts().add(post);
        }
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/blog/{id}")
    public String viewPost(@PathVariable(value = "id") Integer id, Model model) {
        Post post = postService.getPostById(id);

        List<Comment> comments = commentService.getCommentByPostId(id);
        Comment comment = new Comment();

        model.addAttribute("caseId", id);
        model.addAttribute("comment", comment);
        model.addAttribute("listOfComment", comments);
        model.addAttribute("post", post);

        return "post";
    }

    @GetMapping("/user/update/{id}")
    public String updateForm(@PathVariable(value = "id") Integer id, Model model, Authentication authentication) {
        Post post = postService.getPostById(id);
        User user = userService.getUserByEmail(authentication.getName());

        if (post.getAuthor().equals(user.getUserName()) || user.getRole().equals("ROLE_ADMIN")) {
            model.addAttribute("caseId", "update");
            model.addAttribute("post", post);
            return "newform";
        }
        return "redirect:/blog/" + id;
    }

    @GetMapping("/user/deletepost/{id}")
    public String deletePost(@PathVariable(value = "id") Integer id, Authentication authentication) {
        Post post = postService.getPostById(id);
        User user = userService.getUserByEmail(authentication.getName());

        if (post.getAuthor().equals(user.getUserName()) || user.getRole().equals("ROLE_ADMIN")) {
            postService.deletePost(id);
            commentService.deleteCommentByPostId(id);
        }
        return "redirect:/";
    }

    @PostMapping("/user/updatepost/{id}")
    public String updatePost(@PathVariable(value = "id") Integer id, @ModelAttribute("post") Post post, Authentication authentication) {
        Post posts = postService.getPostById(id);
        User user = userService.getUserByEmail(authentication.getName());

        Timestamp instant = Timestamp.from(Instant.now());

        posts.setUserName(authentication.getName());
        posts.setAuthor(user.getUserName());
        posts.setUpdatedAt(instant);
        posts.setTitle(post.getTitle());
        posts.setContent(post.getContent());
        posts.setExcerpt(post.getExcerpt());

        postService.updatePostByID(posts, id);

        return "redirect:/";
    }

    @GetMapping("/page/{pageNo}")
    public String paginatedPage(@PathVariable(value = "pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDirection") String sortDirection, Model model) {
        int pageSize = 10;

        Page<Post> page = postService.findPaginated(pageNo, pageSize, sortField, sortDirection);
        List<Post> listOfPosts = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfPost", listOfPosts);

        return "index";
    }

    @GetMapping("/search")
    public String searching(@RequestParam("search") String keyWord, Model model) {
        Set<Post> setOfPosts = new HashSet<>();

        List<Post> listOfPost = postService.findAllSearchMatchByKeyword(keyWord);
        List<Post> posts = postService.getPostByTagName(keyWord);

        if (posts.size() > 0) {
            listOfPost.addAll(posts);
            for (Post post : listOfPost) {
                setOfPosts.add(post);
            }
            model.addAttribute("listOfPost", setOfPosts);
        } else {
            model.addAttribute("listOfPost", listOfPost);
        }
        return "searchindex";
    }

    @PostMapping("/filter")
    public String filtering(Model model) {
        List<Post> posts = postService.getAllPost();
        List<Tag> tags = tagService.getAllTags();

        Set<String> authorsList = new HashSet<>();
        Set<String> tagsList = new HashSet<>();
        Set<Timestamp> PublishedTime = new HashSet<>();

        for (Post post : posts) {
            authorsList.add(post.getAuthor());
            PublishedTime.add(post.getPublishedAt());
        }
        for (Tag tag : tags) {
            tagsList.add(tag.getName());
        }

        model.addAttribute("time", PublishedTime);
        model.addAttribute("authors", authorsList);
        model.addAttribute("tags", tagsList);

        return "showfilterform";
    }

    @GetMapping("/filtering")
    public String filteringSearch(@RequestParam(value = "authorList", required = false, defaultValue = "")
                                          String[] authorList, Model model,
                                  @RequestParam(value = "tagsList", required = false, defaultValue = "")
                                          String[] tagList, @RequestParam(value = "time", required = false, defaultValue = "") String[] publishedAt) {
        List<Post> posts = new ArrayList<>();
        Set<Post> setOfPost = new HashSet<>();

        for (String name : authorList) {
            posts.addAll(postService.getPostByAuthor(name));
        }
        for (String name : tagList) {
            posts.addAll(postService.getPostByTagName(name));
        }

        for (String time : publishedAt) {
            posts.addAll(postService.getPostByPublishedAt(time));
        }

        for (Post post : posts) {
            setOfPost.add(post);
        }
        model.addAttribute("listOfPost", setOfPost);
        return "searchindex";
    }

    @PostMapping("/admin/saveadminpost")
    public String saveAdminsPost(@RequestParam("tag") String tags, @ModelAttribute("post") Post post, Authentication authentication) {
        String[] tagsArray = tags.split(",");
        List<String> tagsList = Arrays.asList(tagsArray);
        post.setUserName(authentication.getName());

        for (String tagStr : tagsList) {
            Tag tag = new Tag();
            tag.setName(tagStr);
            post.getTags().add(tag);
            tag.getPosts().add(post);
        }
        postService.savePost(post);
        return "redirect:/";
    }

}
