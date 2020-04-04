package com.example.xiaoying.enums;

public enum CommentTypeEnum {
    VIDEO(1),
    COMMENT(2)
    ;

    private Integer type;

    public static boolean
    isEmpty(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType().equals(type)){
                return false;
            }

        }
        return true;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
