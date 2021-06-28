package com.springproject.blogapplication.repository;

import com.springproject.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * from users where email =?1", nativeQuery = true)
    public User getUserByUserName(String userName);

}
