package com.springproject.blogapplication.service;

import com.springproject.blogapplication.model.User;
import com.springproject.blogapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByUserName(email);
    }

}
