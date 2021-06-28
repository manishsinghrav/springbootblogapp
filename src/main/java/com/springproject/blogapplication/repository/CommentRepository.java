package com.springproject.blogapplication.repository;

import com.springproject.blogapplication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.ls.LSOutput;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comments where  post_id =:postId", nativeQuery = true)
    public List<Comment> getCommentByPostId(@Param("postId") Integer postId);

    @Modifying
    @Query(value = "DELETE  FROM comments  where  post_id = :postId", nativeQuery = true)
    public void deleteComment(@Param("postId") Integer postId);

    @Modifying
    @Query(value = "insert into comments(post_id, name, email, comment, created_at,  updated_at) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    public void insertComment(@Param("postId") Integer postId, @Param("name") String name,
                              @Param("email") String email, @Param("comment") String comment,
                              @Param("createdAt") Timestamp createdAt, @Param("updatedAt") Timestamp upDatedAt);

    @Modifying
    @Query(value = "DELETE  FROM comments  where  id = :id", nativeQuery = true)
    public void deleteCommentUsingId(@Param("id") Integer id);

    @Modifying
    @Query(value = "update comments  set name =?1, email=?2, comment=?3 WHERE id =?4", nativeQuery = true)
    public void updatePostById(String name, String email, String comment, Integer id);


    @Query(value = "SELECT post_id FROM comments  where  id =?1", nativeQuery = true)
    public Integer getPostIdUsingCommentId(Integer commentId);


    @Modifying
    @Query(value = "update comments set name =?1, email=?2, comment=?3," +
            " updated_at=?4 WHERE id =?5", nativeQuery = true)
    public void updateCommentByCommentId(@Param("name") String name, @Param("email") String email,
                                         @Param("comment") String comment,
                                         @Param("updatedAt") Timestamp upDatedAt, Integer commentId);

}
