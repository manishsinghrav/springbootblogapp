package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.Post;
import com.springproject.blogapplication.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(Post post) {
        post.setIsPublished("published");
        this.postRepository.save(post);
    }

    @Override
    public Post getPostById(Integer id) {
        Post post1 = postRepository.findById(id).get();
        System.out.println(post1.getTags());

        Optional<Post> optional = postRepository.findById(id);
        Post post = null;

        if (optional.isPresent()) {
            post = optional.get();
        } else {
            throw new RuntimeException("not present id= " + id);
        }
        return post;
    }

    @Override
    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public void updatePostByID(Post post, Integer id) {
        postRepository.save(post);
    }

    @Override
    public Page<Post> findPaginated(Integer pageNo, Integer pageSize, String sortingField, String sortingOrder) {
        Sort sort = sortingOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortingField).ascending() : Sort.by(sortingField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> getPostByAuthor(String name) {
        return  postRepository.findPostByName(name);
    }

    @Override
    public List<Post> getPostByTagName(String tag) {

        return postRepository.findPostByTag(tag);
    }

    @Override
    public List<Post> getPostByPublishedAt(String publishedAt) {

        return postRepository.findPostByPublishedAt(publishedAt);
    }

    @Override
    public List<Post> findAllSearchMatchByKeyword(String keyWord) {

        return postRepository.findAllPostMatchBySearch(keyWord);
    }


}
