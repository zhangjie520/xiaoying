package com.example.xiaoying.enums;

public enum VideoStatusEnum {
    NOTCHECK(1),

    CHECKED(2),
    CHECK_FAIL(3),
    ;

    private Integer status;

    public static boolean isEmpty(Integer status) {
        for (VideoStatusEnum commentTypeEnum : VideoStatusEnum.values()) {
            if (commentTypeEnum.getStatus().equals(status)){
                return false;
            }

        }
        return true;
    }

    public Integer getStatus() {
        return status;
    }

    VideoStatusEnum(Integer status) {
        this.status = status;
    }
}

