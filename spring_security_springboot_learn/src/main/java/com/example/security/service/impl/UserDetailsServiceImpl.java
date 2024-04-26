package com.example.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.security.entity.LoginUser;
import com.example.security.entity.User;
import com.example.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        //(这个异常是有过滤器来捕捉的)
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名错误");
        }

        //TODO 根据用户查询权限信息 添加到LoginUser中

        // 正常来说，权限信息要从数据库中获取，并返回。代码这里就先给定一个权限集合，写死
        ArrayList<String> list = new ArrayList<>(Arrays.asList("test", "admin"));

        //封装成UserDetails对象返回
        return new LoginUser(user, list);
    }
}
