package com.example.xiaoying.controller;

import com.example.xiaoying.dto.NotificationDTO;
import com.example.xiaoying.enums.NotificationTypeEnum;
import com.example.xiaoying.model.User;
import com.example.xiaoying.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String notification(HttpServletRequest request, Model model,
                        @PathVariable("id") Long notificationId) {
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(notificationId,user);
        if (notificationDTO.getType()==NotificationTypeEnum.COMMENT.getType()
                ||notificationDTO.getType()==NotificationTypeEnum.VIDEO.getType()
                ||notificationDTO.getType()==NotificationTypeEnum.CHECK_PASS.getType()
                ||notificationDTO.getType()==NotificationTypeEnum.CHECK_FAIL.getType()){
            return "redirect:/video/"+notificationDTO.getOuterId()+"";
        }
        return "redirect:/";
    }
}
