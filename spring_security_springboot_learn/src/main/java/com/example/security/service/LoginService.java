package com.example.security.service;

import com.example.security.entity.User;
import com.example.security.result.ResponseResult;

public interface LoginService {

    public ResponseResult<User> login(User user);

    public ResponseResult logout();
}
