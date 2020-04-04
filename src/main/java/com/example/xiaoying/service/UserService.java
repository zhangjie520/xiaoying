package com.example.xiaoying.service;

import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.mapper.UserMapper;
import com.example.xiaoying.model.User;
import com.example.xiaoying.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Author: codeape
 * @Date: 2020/3/31 22:21
 * @Version: 1.0
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void insert(User user) {
        UserExample userQueryExample = new UserExample();
        userQueryExample.createCriteria()
                .andNameEqualTo(user.getName());
        List<User> queryUsers = userMapper.selectByExample(userQueryExample);
        if (queryUsers.size()!=0){
            //用户已存在
            throw new CustomizeException(CustomizeErrorCode.USER_IS_EXIST);
        }
        //用户未存在，插入用户
        userMapper.insert(user);
        return;

    }

    public User queryUser(User loginUser) {
        UserExample userQueryExample = new UserExample();
        userQueryExample.createCriteria()
                .andNameEqualTo(loginUser.getName());
        List<User> queryUsers = userMapper.selectByExample(userQueryExample);
        if (queryUsers.size()!=0){
            //用户是注册过的
            UserExample userExample=new UserExample();
            userExample.createCriteria()
                    .andNameEqualTo(loginUser.getName())
                    .andPasswordEqualTo(loginUser.getPassword());
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size()==0){
                throw new CustomizeException(CustomizeErrorCode.PASSWORD_IS_ERROR);
            }
            return users.get(0);
        }else {
            //用户未注册
            throw new CustomizeException(CustomizeErrorCode.USER_IS_NOT_EXIST);
        }
    }
}
