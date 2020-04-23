package com.example.xiaoying.controller;

import com.example.xiaoying.dto.PaginationDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.User;
import com.example.xiaoying.service.NoteService;
import com.example.xiaoying.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: codeape
 * @Date: 2020/3/30 21:12
 * @Version: 1.0
 */
@Controller
public class IndexController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private NoteService noteService;
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer pageIndex,
                        @RequestParam(value = "size", defaultValue = "8") Integer size,
                        @RequestParam(value = "search", required = false) String search,
                        @RequestParam(value = "tag", required = false) String tag){
        if ("recommend".equals(tag)){
            PaginationDTO paginationDTOS = videoService.queryByTag(tag,pageIndex,size);
            model.addAttribute("paginationS", paginationDTOS);
            model.addAttribute("tag",tag);
            return "index";
        }else if ("rank".equals(tag)){
            PaginationDTO paginationDTOS = videoService.queryByTag(tag,pageIndex,size);
            model.addAttribute("paginationS", paginationDTOS);
            model.addAttribute("tag",tag);
            return "index";
        }else if("check".equals(tag)){
            //管理员审核界面只能管理员才能进入
            User user = (User) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getName())){
                throw new CustomizeException(CustomizeErrorCode.REFUSE_ENTRY);
            }
            PaginationDTO paginationDTOS = videoService.queryByTag(tag,pageIndex,size);
            model.addAttribute("paginationS", paginationDTOS);
            model.addAttribute("tag",tag);
            return "check_index";
        }else if ("myVideo".equals(tag)){
            User user = (User) request.getSession().getAttribute("user");
            if (user==null){
                throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
            }
            PaginationDTO paginationDTOS = videoService.queryMyVideo(tag,pageIndex,size,user);
            model.addAttribute("paginationS", paginationDTOS);
            model.addAttribute("tag",tag);
            return "index";
        }else if("my_note".equals(tag)){
            User user = (User) request.getSession().getAttribute("user");
            if (user==null){
                throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
            }
            PaginationDTO paginationDTOS = noteService.queryMyNotes(tag,pageIndex,size,user);
            model.addAttribute("paginationS", paginationDTOS);
            model.addAttribute("tag",tag);
            return "notes";
        }
        PaginationDTO paginationDTOS = videoService.list(search, tag, pageIndex, size);
        model.addAttribute("paginationS", paginationDTOS);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        return "index";
    }
}
