package com.example.xiaoying.service;

import com.example.xiaoying.dto.CommentDTO;
import com.example.xiaoying.enums.CommentTypeEnum;
import com.example.xiaoying.enums.NotificationStatusEnum;
import com.example.xiaoying.enums.NotificationTypeEnum;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.mapper.*;
import com.example.xiaoying.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VideoExtMapper videoExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            //回复的视频不存在,通过处理异常的方式直接返回json,免得返回controller返回json,多一层依赖
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || CommentTypeEnum.isEmpty(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_FOUND);
        }
        if (comment.getType() == CommentTypeEnum.VIDEO.getType()) {
            //回复视频
            Video video = videoMapper.selectByPrimaryKey(comment.getParentId());
            if (video == null) {
                throw new CustomizeException(CustomizeErrorCode.VIDEO_NOT_FOUND);
            }
            comment.setCommentCount(0L);
            commentMapper.insert(comment);
            video.setCommentCount(1L);
            videoExtMapper.incCommentCount(video);
            createNotification(comment, Long.valueOf(video.getAccountId()), NotificationTypeEnum.VIDEO.getType(), Long.valueOf(video.getVideoId()));


        } else {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1L);
            commentExtMapper.incCommentCount(parentComment);
            Video video = videoMapper.selectByPrimaryKey(dbComment.getParentId());
            createNotification(comment, dbComment.getCommentor(), NotificationTypeEnum.COMMENT.getType(), Long.valueOf(video.getVideoId()));
        }
    }

    /**
     * 在回复视频和评论时写入一条通知
     * @param comment
     * @param receiver
     * @param type
     * @param videoId
     */
    private void createNotification(Comment comment, Long receiver, Integer type, Long videoId) {
        if (receiver==comment.getCommentor()){
            return ;
        }
        Notification notification = new Notification();
        User notifier = userMapper.selectByPrimaryKey(comment.getCommentor());
        notification.setNotifier(Long.valueOf(notifier.getAccountId()));
        notification.setReceiver(receiver);
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setOuterId(videoId);
        notification.setStatus(NotificationStatusEnum.NOTREADED.getStatus());
        notification.setType(type);
        notificationMapper.insert(notification);
    }


    public List<CommentDTO> listByVideoId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
//        List<CommentDTO> commentDTOS=new ArrayList<>();
//        for (Comment comment : comments) {
//            User user = userMapper.selectByPrimaryKey(comment.getCommentor());
//            CommentDTO commentDTO=new CommentDTO();
//            commentDTO.setUser(user);
//            BeanUtils.copyProperties(comment,commentDTO);
//            commentDTOS.add(commentDTO);
//        }
        //优化，因为有些评论user是一个，所以用lombda,set获取去重后的userIds
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentor()).distinct().collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //将users转换为usermap，避免用for判断user.id==comment.commentator造成n平方时间复杂度
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentor()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    public void incLikeCount(Long id) {
        Comment comment = commentMapper.selectByPrimaryKey(Long.valueOf(id));
        if (comment==null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
        comment.setId(id);
        comment.setLikeCount(1L);
        commentExtMapper.incLikeCount(comment);
    }
}
