package com.springproject.blogapplication.service;

import com.springproject.blogapplication.config.CustomUserDetails;
import com.springproject.blogapplication.model.User;
import com.springproject.blogapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.getUserByUserName(userName);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return customUserDetails;
    }
}
