package com.example.xiaoying.cache;

import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.model.Video;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class RecommendVideoCache {
    private List<VideoDTO>
            videoDTOS;
    private static final int RECOMMEND_VIDEOS_LENGTH = 16;

    public void sortVideo(List<VideoDTO> videos) {
        Queue priorities = new PriorityQueue(RECOMMEND_VIDEOS_LENGTH);
        videos.forEach(
                video -> {
                    VideoDTO videoDTO = new VideoDTO();
                    BeanUtils.copyProperties(video,videoDTO);
                    if (priorities.size() < RECOMMEND_VIDEOS_LENGTH) {
                        priorities.add(videoDTO);
                    } else {
                        Video  minVideo= (Video) priorities.peek();
                        if (videoDTO.compareTo(minVideo) > 0) {
                            priorities.poll();
                            priorities.add(minVideo);
                        }
                    }
                }
        );
        List<VideoDTO> sortedVideos = new ArrayList();
        VideoDTO poll = (VideoDTO) priorities.poll();
        while (poll != null) {
            sortedVideos.add(0, poll);
            poll = (VideoDTO) priorities.poll();
        }
        this.videoDTOS = sortedVideos;
    }
}
