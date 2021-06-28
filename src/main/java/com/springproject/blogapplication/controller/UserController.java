package com.springproject.blogapplication.controller;

import com.springproject.blogapplication.model.User;
import com.springproject.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        User user=new User();
        model.addAttribute("user", user);
        return "registerform";
    }

    @PostMapping("/register")
    public String saveRegisterInfo(@ModelAttribute("user") User user){
        user.setRole("ROLE_AUTHOR");
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        userService.saveUser(user);
        return "redirect:/";
    }
}
