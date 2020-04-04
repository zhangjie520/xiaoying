package com.example.xiaoying.dto;

import com.example.xiaoying.model.User;
import com.example.xiaoying.model.Video;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long notifier;
    private Long receiver;
    private Long outerId;//当是回复question时是question的Id，当回复评论时是评论对应question的id
    private Long gmtCreate;
    private Integer type;
    private Integer status;
    private String typeValue;
    private User notifierUser;
    private Video video;
}
