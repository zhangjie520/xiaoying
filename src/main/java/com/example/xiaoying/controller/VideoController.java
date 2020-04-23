package com.example.xiaoying.controller;

import com.example.xiaoying.dto.CommentDTO;
import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.enums.CommentTypeEnum;
import com.example.xiaoying.enums.VideoStatusEnum;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.model.Note;
import com.example.xiaoying.model.User;
import com.example.xiaoying.model.Video;
import com.example.xiaoying.provider.FileProvider;
import com.example.xiaoying.provider.UUIdProvider;
import com.example.xiaoying.service.CommentService;
import com.example.xiaoying.service.NoteService;
import com.example.xiaoying.service.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: codeape
 * @Date: 2020/4/1 4:32
 * @Version: 1.0
 */
@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NoteService noteService;

    @GetMapping("videoUpload")
    public String videoUpload(){
        return "videoUpload";
    }

    @RequestMapping(value = "videoUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO savaVideo(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "title",required = false) String title,
                                        @RequestParam(value = "video_tag",required = false) String video_tag,
                                        @RequestParam(value = "video_bio",required = false) String video_bio,
                                        @RequestParam(value = "video_cover_url",required = false) String video_cover,
                                        HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (user==null){
            ResultDTO resultDTO=ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            return resultDTO;
        }
        if (title==null||StringUtils.isBlank(title)){
            return ResultDTO.errorOf(CustomizeErrorCode.TITLE_IS_EMPTY);
        }
        if (video_tag==null||StringUtils.isBlank(video_tag)){
            return ResultDTO.errorOf(CustomizeErrorCode.VIDEO_TAG_IS_EMPTY);
        }
        if (video_bio==null||StringUtils.isBlank(video_bio)){
            return ResultDTO.errorOf(CustomizeErrorCode.VIDEO_BIO_IS_EMPTY);
        }
        if (video_cover==null||StringUtils.isBlank(video_cover)){
            return ResultDTO.errorOf(CustomizeErrorCode.VIDEO_COVER_IS_EMPTY);
        }
        String newVideoName = FileProvider.save(file,"video");
        if (newVideoName!=null){
            //构建video，存入数据库
            Video video=new Video();
            video.setAccountId(user.getAccountId());
            video.setVideoId(UUIdProvider.getAccountIdByUUId());
            video.setVideoTitle(title);
            video.setVideoBio(video_bio);
            video.setVideoTag(video_tag);
            video.setVideoCover(video_cover);
            video.setVideoUrl(newVideoName);
            video.setStatus(VideoStatusEnum.NOTCHECK.getStatus()+"");
            video.setGmtCreate(System.currentTimeMillis());
            video.setGmtModified(System.currentTimeMillis());
            video.setLikeCount(0L);
            video.setViewCount(0L);
            video.setCommentCount(0L);
            videoService.insert(video);
            return ResultDTO.okOf(video);
        }
        return null;
    }
    @PostMapping("/uploadCover")
    @ResponseBody
    public ResultDTO uploadCover(@RequestParam("file") MultipartFile file)
            throws IllegalStateException {
        Map<String,String> resultMap = new HashMap<>();
        String coverUrl = FileProvider.save(file,"cover");
        if (coverUrl!=null){
            resultMap.put("coverUrl",coverUrl);
            return  ResultDTO.okOf(resultMap);
        }
        return  null;
    }
    @GetMapping("/video/{videoId}")
    public String video(@PathVariable("videoId") String videoId,
                        Model model){
        VideoDTO videoDTO=videoService.queryByVideoId(videoId);
        videoService.incViewCount(videoId);
        List<VideoDTO> relatedVideos = videoService.selectRelated(videoDTO);
        List<CommentDTO> comments=commentService.listByVideoId(videoDTO.getId(), CommentTypeEnum.VIDEO);
        model.addAttribute("comments",comments);
        model.addAttribute("video",videoDTO);
        model.addAttribute("relatedVideos",relatedVideos);
        return "video";
    }
    @GetMapping("/likeVideo/{id}")
    @ResponseBody
    public ResultDTO likeVideo(@PathVariable("id") Long id){
        videoService.incLikeCount(id);
        return ResultDTO.okOf();
    }
    @GetMapping("/video/record_model/{videoId}")
    public String videoRecord(@PathVariable("videoId") String videoId,Model model,
                              HttpServletRequest request){
        VideoDTO videoDTO=videoService.queryByVideoId(videoId);
        User user=(User)request.getSession().getAttribute("user");
        if (user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Note note = noteService.queryNoteByVideoIdAndUser(videoId, user);
        if (note!=null){
            model.addAttribute("note",note);
        }
        model.addAttribute("video",videoDTO);
        return "videoRecord";
    }
}
