package com.springproject.blogapplication.repository;

import com.springproject.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM posts p WHERE p.author LIKE %?1% OR p.title LIKE %?1% OR p.excerpt LIKE %?1%" +
            " OR p.content LIKE %?1% ")
    public List<Post> findAllPostMatchBySearch(String keyWord);

    @Query(value ="select * from blog.posts where id in (select post_id from blog.post_tags where tag_id in" +
            " (select id from blog.tags where name=?1))",nativeQuery = true)
    public List<Post> findPostByTag(String tag);
    
    @Query("SELECT p FROM posts p WHERE p.author=:name")
    public List<Post>findPostByName(@Param("name") String name);

    @Query(value = "SELECT * FROM posts WHERE published_at =:publishedAt ",nativeQuery = true)
    public List<Post>findPostByPublishedAt(@Param("publishedAt") String publishedAt);

}
