package com.example.xiaoying.dto;


import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;
    public static ResultDTO errorof(Integer code,String message){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(noLogin.getCode());
        resultDTO.setMessage(noLogin.getMessage());
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setMessage(e.getMessage());
        resultDTO.setCode(e.getCode());
        return resultDTO;
    }

    public static ResultDTO
    okOf() {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);

        return resultDTO;
    }
}
