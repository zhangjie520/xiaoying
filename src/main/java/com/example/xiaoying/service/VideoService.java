package com.example.xiaoying.service;

import com.example.xiaoying.cache.RecommendVideoCache;
import com.example.xiaoying.dto.PaginationDTO;
import com.example.xiaoying.dto.VideoDTO;
import com.example.xiaoying.dto.VideoQueryDTO;
import com.example.xiaoying.enums.NotificationStatusEnum;
import com.example.xiaoying.enums.NotificationTypeEnum;
import com.example.xiaoying.enums.VideoStatusEnum;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.mapper.NotificationMapper;
import com.example.xiaoying.mapper.UserMapper;
import com.example.xiaoying.mapper.VideoExtMapper;
import com.example.xiaoying.mapper.VideoMapper;
import com.example.xiaoying.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: codeape
 * @Date: 2020/4/2 0:50
 * @Version: 1.0
 */
@Service
public class VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoExtMapper videoExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private RecommendVideoCache recommendVideoCache;

    public void insert(Video video) {
        videoMapper.insert(video);
    }

    public PaginationDTO list(String search, String tag, Integer pageIndex, Integer size) {
        List<VideoDTO> videoDTOS=new ArrayList<>();
        PaginationDTO paginationDTO=new PaginationDTO();
        if (StringUtils.isNotBlank(search)){
            String[] searchs = search.split(" ");
            search = Arrays.stream(searchs).collect(Collectors.joining("|"));
        }

        VideoQueryDTO videoQueryDTO=new VideoQueryDTO();
        if (StringUtils.isBlank(search)){
            search=null;
        }
        if (StringUtils.isBlank(tag)){
            tag=null;
        }
        videoQueryDTO.setSearch(search);
        videoQueryDTO.setTag(tag);
        videoQueryDTO.setStatus(VideoStatusEnum.CHECKED.getStatus()+"");
        Integer totalCount=videoExtMapper.countBySearch(videoQueryDTO);
        //计算总页数
        Integer totalPage;

        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=(totalCount/size)+1;
        }
        if (totalCount==0){
            totalPage=1;
        }

        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        paginationDTO.setData(videoDTOS);
        //查询分页数据
        Integer offset=(pageIndex-1)*size;
        videoQueryDTO.setPageIndex(offset);
        videoQueryDTO.setSize(size);
        videoQueryDTO.setStatus(VideoStatusEnum.CHECKED.getStatus()+"");
        List<Video> videos=videoExtMapper.selectBySearch(videoQueryDTO);

        for (Video video :
                videos) {
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(video.getAccountId());
            User user =userMapper.selectByExample(userExample).get(0);
            VideoDTO videoDTO=new VideoDTO();
            BeanUtils.copyProperties(video,videoDTO);
            videoDTO.setUser(user);
            videoDTOS.add(videoDTO);
        }

        return paginationDTO;
    }


    public VideoDTO queryByVideoId(String videoId) {
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria()
                .andVideoIdEqualTo(videoId);
        Video video = videoMapper.selectByExample(videoExample).get(0);
        if (video==null){
            throw new CustomizeException(CustomizeErrorCode.VIDEO_NOT_FOUND);
        }
        VideoDTO videoDTO=new VideoDTO();
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(video.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        BeanUtils.copyProperties(video,videoDTO);
        videoDTO.setUser(users.get(0));
        return videoDTO;
    }


    public void incViewCount(String videoId) {

        Video video = new Video();
        video.setVideoId(videoId);
        video.setViewCount(1L);
        videoExtMapper.incViewCount(video);
    }

    public List<VideoDTO> selectRelated(VideoDTO queryVideoDTO) {
        //相同标签下的视频按照浏览量推荐
        String[] tags=queryVideoDTO.getVideoTag().split(",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Video queryVideo=new Video();
        queryVideo.setVideoId(queryVideoDTO.getVideoId());
        queryVideo.setVideoTag(regexpTag);
        queryVideo.setStatus(VideoStatusEnum.CHECKED.getStatus()+"");
        List<Video> videoRelated = videoExtMapper.selectRelated(queryVideo);
        //相同标签下的视频不足三个，选择浏览量最高的视频补充
        if(videoRelated.size()<3){
            VideoExample videoSupplyExample = new VideoExample();
            videoSupplyExample.setOrderByClause("like_count desc");
            videoSupplyExample.createCriteria()
                    .andStatusEqualTo(VideoStatusEnum.CHECKED.getStatus()+"")
                    .andVideoIdNotEqualTo(queryVideoDTO.getVideoId());
            List<Video> supplyVideos = videoMapper.selectByExampleWithRowbounds(videoSupplyExample, new RowBounds(0, 3 - videoRelated.size()));
            videoRelated.addAll(supplyVideos);
        }
        List<VideoDTO> videoDTOList = videoRelated.stream().map(video -> {
            VideoDTO videoDTO = new VideoDTO();
            BeanUtils.copyProperties(video, videoDTO);
            return videoDTO;
        }).collect(Collectors.toList());
        return videoDTOList;
    }

    public PaginationDTO queryByTag(String tag,Integer pageIndex,Integer size) {
        List<VideoDTO> videoDTOS=new ArrayList();
        List<Video> videos=null;
        if ("check".equals(tag)){
            //如果是管理员来审核视频，则查询未审核的视频
            VideoExample unCheckExample = new VideoExample();
            unCheckExample.createCriteria()
                    .andStatusEqualTo(VideoStatusEnum.NOTCHECK.getStatus()+"");
            unCheckExample.setOrderByClause("gmt_create desc");
            videos= videoMapper.selectByExample(unCheckExample);
        }else{
            //根据tag排序,优化：放入定时器
            VideoExample videoExample = new VideoExample();
            videoExample.createCriteria()
                    .andStatusEqualTo(VideoStatusEnum.CHECKED.getStatus()+"");
            videoExample.setOrderByClause("view_count desc");
            videos = videoMapper.selectByExample(videoExample);

        }

        //获取video中的user
        for (Video video :
                videos) {
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(video.getAccountId());
            User user =userMapper.selectByExample(userExample).get(0);
            VideoDTO videoDTO=new VideoDTO();
            BeanUtils.copyProperties(video,videoDTO);
            videoDTO.setUser(user);
            videoDTOS.add(videoDTO);
        }
        if("recommend".equals(tag)){
            //如果是推荐，用优先队列
            recommendVideoCache.sortVideo(videoDTOS);
            videoDTOS=recommendVideoCache.getVideoDTOS();
        }else if ("rank".equals(tag)){
            //如果是排名，直接取从offset开始的size个

        }

        //排序后的数据分页
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=videoDTOS.size();
        //计算总页数
        Integer totalPage;

        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=(totalCount/size)+1;
        }
        if (totalCount==0){
            totalPage=1;
        }

        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        //将数据分页返回
        Integer offset=(pageIndex-1)*size;
        if (videoDTOS.size()-offset>size){
            videoDTOS=videoDTOS.subList(offset,offset+size);
        }else {
            videoDTOS=videoDTOS.subList(offset, videoDTOS.size());
        }

        paginationDTO.setData(videoDTOS);

        return paginationDTO;
    }

    public void incLikeCount(Long id) {
        Video video = videoMapper.selectByPrimaryKey(Long.valueOf(id));
        if (video==null){
            throw new CustomizeException(CustomizeErrorCode.VIDEO_NOT_FOUND);
        }
        video.setLikeCount(1L);
        video.setId(id);
        videoExtMapper.incLikeCount(video);
    }

    public void updateVideoStatus(Long id, User user) {
        Video video = videoMapper.selectByPrimaryKey(Long.valueOf(id));
        if (video==null){
            throw new CustomizeException(CustomizeErrorCode.VIDEO_NOT_FOUND);
        }
        video.setStatus(VideoStatusEnum.CHECKED.getStatus()+"");
        videoExtMapper.updateVideoStatus(video);
        //管理员通过审核，向用户发送一条通知
        createNotification(user, video, NotificationTypeEnum.CHECK_PASS.getType());
    }

    private void createNotification(User user, Video video, Integer type) {
        Notification notification = new Notification();
        notification.setNotifier(Long.valueOf(user.getAccountId()));
        notification.setReceiver(Long.valueOf(video.getAccountId()));
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setOuterId(Long.valueOf(video.getVideoId()));
        notification.setStatus(NotificationStatusEnum.NOTREADED.getStatus());
        notification.setType(type);
        notificationMapper.insert(notification);
    }

    public void updateVideoStatusToFail(Long id, User user) {
        Video video = videoMapper.selectByPrimaryKey(Long.valueOf(id));
        if (video==null){
            throw new CustomizeException(CustomizeErrorCode.VIDEO_NOT_FOUND);
        }
        //视频不能删掉，不然用户收到通知，消息里无法获得视频信息，所以将视频改变状态
        video.setStatus(VideoStatusEnum.CHECK_FAIL.getStatus()+"");
        videoExtMapper.updateVideoStatus(video);
        //管理员拒绝通过审核，向用户发送一条通知
        createNotification(user,video,NotificationTypeEnum.CHECK_FAIL.getType());

    }

    public PaginationDTO queryMyVideo(String tag, Integer pageIndex, Integer size, User user) {
        List<VideoDTO> videoDTOS=new ArrayList();
        List<Video> videos=null;
        VideoExample myVideoExample = new VideoExample();
        myVideoExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        myVideoExample.setOrderByClause("gmt_create desc");
        videos= videoMapper.selectByExample(myVideoExample);
        //获取video中的user
        for (Video video :
                videos) {
            VideoDTO videoDTO=new VideoDTO();
            BeanUtils.copyProperties(video,videoDTO);
            videoDTO.setUser(user);
            videoDTOS.add(videoDTO);
        }
        //排序后的数据分页
        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalCount=videoDTOS.size();
        //计算总页数
        Integer totalPage;

        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=(totalCount/size)+1;
        }
        if (totalCount==0){
            totalPage=1;
        }

        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex<1){
            pageIndex=1;
        }else if(pageIndex>totalPage){
            pageIndex=totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);
        //将数据分页返回
        Integer offset=(pageIndex-1)*size;
        if (videoDTOS.size()-offset>size){
            videoDTOS=videoDTOS.subList(offset,offset+size);
        }else {
            videoDTOS=videoDTOS.subList(offset, videoDTOS.size());
        }

        paginationDTO.setData(videoDTOS);


        return paginationDTO;
    }
}
