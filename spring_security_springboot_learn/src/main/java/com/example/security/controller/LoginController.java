package com.example.security.controller;

import com.example.security.entity.User;
import com.example.security.result.ResponseResult;
import com.example.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult<User> login(@RequestBody User user){
        return loginService.login(user);
    }

    @GetMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
