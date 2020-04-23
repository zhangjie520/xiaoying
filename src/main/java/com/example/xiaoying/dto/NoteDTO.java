package com.example.xiaoying.dto;

import com.example.xiaoying.model.User;
import lombok.Data;

@Data
public class NoteDTO {
    private Long id;
    private Long videoId;
    private String title;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private User user;
}