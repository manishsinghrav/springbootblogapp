package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.User;

public interface UserService  {
    public void saveUser(User user);
    public User getUserByEmail(String email);
}
