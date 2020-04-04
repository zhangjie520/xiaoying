package com.example.xiaoying.controller;

import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.User;
import com.example.xiaoying.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: codeape
 * @Date: 2020/4/3 4:05
 * @Version: 1.0
 */
@Controller
public class CheckController {
    @Autowired
    private VideoService videoService;
    @GetMapping("/check/{id}")
    public String check(@PathVariable(value = "id",required = false) String id,
                        Model model){
        VideoDTO videoDTO = videoService.queryByVideoId(id);
        model.addAttribute("video",videoDTO);
        return "check";
    }
    @GetMapping("/checkPass/{id}")
    @ResponseBody
    public ResultDTO checkPass(@PathVariable(value = "id",required = false) Long id,
                               HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (!"admin".equals(user.getName())){
            throw new CustomizeException(CustomizeErrorCode.REFUSE_ENTRY);
        }
        videoService.updateVideoStatus(id,user);
        return ResultDTO.okOf();
    }
    @GetMapping("/checkFail/{id}")
    @ResponseBody
    public ResultDTO checkFail(@PathVariable(value = "id",required = false) Long id,
                               HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (!"admin".equals(user.getName())){
            throw new CustomizeException(CustomizeErrorCode.REFUSE_ENTRY);
        }
        videoService.updateVideoStatusToFail(id,user);
        return ResultDTO.okOf();
    }
}
