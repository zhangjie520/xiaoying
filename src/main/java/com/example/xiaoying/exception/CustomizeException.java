package com.example.xiaoying.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message=errorCode.getMessage();
        this.code=errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
