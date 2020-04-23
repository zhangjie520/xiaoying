package com.example.xiaoying.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: codeape
 * @Date: 2020/4/19 2:29
 * @Version: 1.0
 */
@Data
public class NoteCreateDTO {
    private Long videoId;
    private String recordContent;
}
