package com.example.xiaoying.enums;

public enum NotificationTypeEnum {
    VIDEO(1,"回复了视频"),
    COMMENT(2,"回复了评论"),
    CHECK_PASS(3,"管理员通过了视频"),
    CHECK_FAIL(4,"管理员拒绝通过视频"),
    ;

    public Integer getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    private Integer type;
    private String data;

    NotificationTypeEnum(Integer type, String data) {
        this.type = type;
        this.data = data;
    }
    public static String nameOfType(Integer type){
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType()==type){
                return notificationTypeEnum.getData();
            }
        }
        return "";
    }
}
