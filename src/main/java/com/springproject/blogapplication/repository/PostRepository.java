package com.springproject.blogapplication.repository;

import com.springproject.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * FROM posts  WHERE author LIKE %?1% OR title LIKE %?1% OR excerpt LIKE %?1%" +
            " OR content LIKE %?1% ",nativeQuery = true)
    public List<Post> findAllPostMatchBySearch(String keyWord);

    @Query(value ="select * from posts where id in (select post_id from post_tags where tag_id in" +
            " (select id from tags where name=?1))",nativeQuery = true)
    public List<Post> findPostByTag(String tag);
    
    @Query("SELECT p FROM posts p WHERE p.author=:name")
    public List<Post>findPostByName(@Param("name") String name);

    @Query(value = "SELECT * FROM posts WHERE published_at =:publishedAt ",nativeQuery = true)
    public List<Post>findPostByPublishedAt(@Param("publishedAt") Timestamp publishedAt);

}
