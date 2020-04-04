package com.example.xiaoying.dto;

import com.example.xiaoying.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentor;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private User user;
    private Long commentCount;
}
