package com.example.xiaoying.controller;

import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.User;
import com.example.xiaoying.provider.UUIdProvider;
import com.example.xiaoying.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: codeape
 * @Date: 2020/3/31 16:28
 * @Version: 1.0
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object loginIn(@RequestBody(required = false) User loginUser,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model){
        if (loginUser==null||StringUtils.isBlank(loginUser.getName())){
            return ResultDTO.errorOf(CustomizeErrorCode.NAME_IS_EMPTY);
        }
        if (loginUser==null||StringUtils.isBlank(loginUser.getPassword())){
            return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_IS_EMPTY);
        }
        User user = userService.queryUser(loginUser);
        response.addCookie(new Cookie("accountId", user.getAccountId()));
        request.getSession().setAttribute("user",user);
        return ResultDTO.okOf();
    }

    @GetMapping("/register")
    public String registerGet(){
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public Object register(@RequestBody(required = false) User registerUser,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response){
        if (registerUser==null||StringUtils.isBlank(registerUser.getName())){
            return ResultDTO.errorOf(CustomizeErrorCode.NAME_IS_EMPTY);
        }
        if (registerUser.getName().length()>10){
            return ResultDTO.errorOf(CustomizeErrorCode.NAME_IS_TOO_LONG);
        }
        if (registerUser==null||StringUtils.isBlank(registerUser.getPassword())){
            return ResultDTO.errorOf(CustomizeErrorCode.PASSWORD_IS_EMPTY);
        }
        User user = new User();
        user.setName(registerUser.getName());
        user.setPassword(registerUser.getPassword());
        user.setAccountId(UUIdProvider.getAccountIdByUUId());
        user.setAvatarUrl("/images/leizong.jpg");
        user.setBio("这个人很懒，什么都没有说");
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(System.currentTimeMillis());
        userService.insert(user);
        response.addCookie(new Cookie("accountId", user.getAccountId()));
        request.getSession().setAttribute("user",user);
        return ResultDTO.okOf();
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("accountId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
