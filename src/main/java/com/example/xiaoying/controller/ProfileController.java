package com.example.xiaoying.controller;

import com.example.xiaoying.dto.NotificationDTO;
import com.example.xiaoying.dto.PaginationDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.User;
import com.example.xiaoying.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Controller
public class ProfileController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile")
    public String profile(Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer pageIndex,
                          @RequestParam(value = "size", defaultValue = "7") Integer size,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        PaginationDTO<NotificationDTO> paginationDTO = notificationService.listByReceiver(user, pageIndex, size);
        int unreadCount = paginationDTO.getData().stream().filter(notificationDTO -> notificationDTO.getStatus() == 0).collect(Collectors.toList()).size();
        model.addAttribute("paginationS", paginationDTO);
        request.getSession().setAttribute("unreadCount",unreadCount);
        return "profile";
    }
}
