package com.example.xiaoying.dto;

import com.example.xiaoying.model.User;
import lombok.Data;

@Data
public class VideoDTO implements Comparable {
    private Long id;
    private String accountId;
    private String videoId;
    private String videoTitle;
    private String videoTag;
    private String videoBio;
    private String videoCover;
    private String videoUrl;
    private String status;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long gmtCreate;
    private Long gmtModified;
    private User user;

    @Override
    public int compareTo(Object o) {
        VideoDTO videoDTO=(VideoDTO)o;
        //如果是推荐的video
        if (((this.getViewCount()-videoDTO.getViewCount())*2+
                (this.getCommentCount()-videoDTO.getCommentCount())*3+
                (this.getLikeCount()-videoDTO.getLikeCount())*5)>0){
            return 1;
        }else{
            return -1;
        }
    }

}