package com.example.security.service.impl;

import com.example.security.entity.LoginUser;
import com.example.security.entity.User;
import com.example.security.result.ResponseResult;
import com.example.security.service.LoginService;
import com.example.security.utils.JwtUtil;
import com.example.security.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate 认证
        //authenticate方法需要Authentication类型对象，这里使用其实现类：UsernamePasswordAuthenticationToken。传入用户名用于查询用户数据，传入密码用于认证
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过
        if(Objects.isNull(authenticate)){
            //抛出异常提示
            throw new RuntimeException("用户名或密码错误-GSF");
        }
        //如果认证通过了，使用userId生成JWT
        //loadUserByUsername返回的LoginUser会被封装到principal中去
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId, loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult logout() {
        // TODO 保证当退出登录后，原先的token失效：（1）获取token的创建日期和数据库中该对象的退出日期，相比较；（2）改变加密算法的盐值

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"退出成功");

    }
}