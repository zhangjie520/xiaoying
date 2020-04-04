package com.example.xiaoying.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    VIDEO_NOT_FOUND( 2001,"您请求的视频不存在"),
    TARGET_PARAM_NOT_FOUND( 2002,"您请求的视频或评论不存在了"),
    NO_LOGIN( 2003,"未登录,请登录后重试"),
    SYS_ERROR( 2004,"服务冒烟，请您稍后试试"),
    TYPE_PARAM_NOT_FOUND( 2005,"您回复评论的类型不存在"),
    COMMENT_NOT_FOUND( 2006,"宁回复的评论不存在"),
    CONTENT_IS_EMPTY(2007,"您评论的内容不能为空"),
    NOTIFIER_NOT_FOUND(2008,"您访问的通知不见了！"),
    NOTIFIER_NOT_MATCH(2009,"这不是你的通知"),
    FILE_UPLOAD_FAIL(2010,"文件上传失败"),
    USER_IS_EXIST(2011,"该用户名已存在"),
    NAME_IS_EMPTY(2012,"用户名为空"),
    PASSWORD_IS_EMPTY(2013,"密码为空"),
    USER_IS_NOT_EXIST(2014,"用户还未注册，请先注册"),
    PASSWORD_IS_ERROR(2015,"密码输入错误"),
    TITLE_IS_EMPTY(2016,"标题不能为空"),
    VIDEO_TAG_IS_EMPTY(2017,"标签不能为空"),
    VIDEO_BIO_IS_EMPTY(2017,"视频简介不能为空"),
    VIDEO_COVER_IS_EMPTY(2017,"视频封面不能为空"),
    NAME_IS_TOO_LONG(2018,"视频名字太长了"),
    REFUSE_ENTRY(2019,"该功能拒绝您的请求"),
    ;
    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
