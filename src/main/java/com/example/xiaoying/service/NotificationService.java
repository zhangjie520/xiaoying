package com.example.xiaoying.service;

import com.example.xiaoying.dto.NotificationDTO;
import com.example.xiaoying.dto.PaginationDTO;
import com.example.xiaoying.enums.NotificationStatusEnum;
import com.example.xiaoying.enums.NotificationTypeEnum;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import com.example.xiaoying.mapper.CommentMapper;
import com.example.xiaoying.mapper.NotificationMapper;
import com.example.xiaoying.mapper.UserMapper;
import com.example.xiaoying.mapper.VideoMapper;
import com.example.xiaoying.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO<NotificationDTO> listByReceiver(User user, Integer pageIndex, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        NotificationExample.Criteria criteria = notificationExample.createCriteria();
        criteria.andReceiverEqualTo(Long.valueOf(user.getAccountId()));
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        //计算总页数
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //如果用户手动输入页码导致小于1或者大于最大页数，则显示最小最大页数数据
        if (pageIndex < 1) {
            pageIndex = 1;
        } else if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setPagination(pageIndex);


        //查询分页数据
        Integer offset = (pageIndex - 1) * size;
        notificationExample.setOrderByClause("gmt_create desc");
        //查询未读通知
//        criteria.andStatusEqualTo(NotificationStatusEnum.NOTREADED.getStatus());
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));

        List<NotificationDTO> notificationDTOS = notifications.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(String.valueOf(notification.getNotifier()));
            List<User> users = userMapper.selectByExample(userExample);
            notificationDTO.setNotifierUser(users.get(0));
            VideoExample example = new VideoExample();
            example.createCriteria()
                    .andVideoIdEqualTo(String.valueOf(notification.getOuterId()));
            Video video = videoMapper.selectByExample(example).get(0);
            notificationDTO.setVideo(video);
            notificationDTO.setTypeValue(NotificationTypeEnum.nameOfType(notification.getType()));
            return notificationDTO;
        }).collect(Collectors.toList());
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public NotificationDTO read(Long notificationId, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if (notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFIER_NOT_FOUND);
        }
        if (!(notification.getReceiver()+"").equals(user.getAccountId())){
            throw new CustomizeException(CustomizeErrorCode.NOTIFIER_NOT_MATCH);
        }
        Notification updateNotification=new Notification();
        updateNotification.setId(notificationId);
        updateNotification.setStatus(NotificationStatusEnum.READED.getStatus());

        notificationMapper.updateByPrimaryKeySelective(updateNotification);
        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        return notificationDTO;
    }

    public Long unReadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatusEnum.NOTREADED.getStatus());
        long unReadCount = notificationMapper.countByExample(notificationExample);
        return unReadCount;
    }
}
