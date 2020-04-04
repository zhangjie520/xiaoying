package com.example.xiaoying.dto;

import lombok.Data;

@Data
public class VideoQueryDTO {
    private String search;
    private String tag;
    private Integer pageIndex;
    private Integer size;
    private String status;
}
