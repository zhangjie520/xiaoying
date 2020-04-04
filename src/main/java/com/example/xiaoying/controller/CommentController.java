package com.example.xiaoying.controller;

import com.example.xiaoying.dto.CommentCreateDTO;
import com.example.xiaoying.dto.CommentDTO;
import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.enums.CommentTypeEnum;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.model.Comment;
import com.example.xiaoying.model.User;
import com.example.xiaoying.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/comment")
    @ResponseBody
    public Object postComment(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            ResultDTO resultDTO=ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            return resultDTO;
        }
        if (commentCreateDTO ==null||StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentCount(0L);
        comment.setCommentor(user.getId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
    @GetMapping("/comment/{id}")
    @ResponseBody
    public ResultDTO<List<CommentDTO>> comments(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByVideoId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
    @GetMapping("/likeComment/{id}")
    @ResponseBody
    public ResultDTO likeComment(@PathVariable("id") Long id){
        commentService.incLikeCount(id);
        return ResultDTO.okOf();
    }
}
