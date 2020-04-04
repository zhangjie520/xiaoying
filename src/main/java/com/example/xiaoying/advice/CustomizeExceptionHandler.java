package com.example.xiaoying.advice;

import com.alibaba.fastjson.JSON;
import com.example.xiaoying.dto.ResultDTO;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(HttpServletRequest request, Throwable e, Model model,
                                           HttpServletResponse response) {
//        HttpStatus status = getStatus(request);
        String contentType=request.getContentType();
        if ("application/json".equals(contentType)){
            ResultDTO resultDTO=null;
            if (e instanceof CustomizeException){
                resultDTO=ResultDTO.errorOf((CustomizeException) e);
            }else {
                resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            PrintWriter writer = null;
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                response.setStatus(200);
                writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }else {
            if (e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message","服务冒烟了...");
            }
            return new ModelAndView("error");
        }

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
