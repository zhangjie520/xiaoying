package com.example.xiaoying.mapper;

import com.example.xiaoying.dto.VideoQueryDTO;
import com.example.xiaoying.model.Video;

import java.util.List;

/**
 * @Author: codeape
 * @Date: 2020/4/2 4:06
 * @Version: 1.0
 */
public interface VideoExtMapper {
    Integer countBySearch(VideoQueryDTO videoQueryDTO);
    List<Video> selectBySearch(VideoQueryDTO videoQueryDTO);

    Integer incViewCount(Video video);
    Integer incCommentCount(Video video);
    Integer incLikeCount(Video video);
    List<Video> selectRelated(Video video);

    Integer updateVideoStatus(Video video);
}
