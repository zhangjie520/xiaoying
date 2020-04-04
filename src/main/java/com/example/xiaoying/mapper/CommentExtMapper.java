package com.example.xiaoying.mapper;

import com.example.xiaoying.model.Comment;

public interface CommentExtMapper {
    Integer incCommentCount(Comment comment);
    Integer incLikeCount(Comment comment);
}