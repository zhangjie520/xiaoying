package com.example.xiaoying.provider;

import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: codeape
 * @Date: 2020/4/1 19:30
 * @Version: 1.0
 */
@Component
public class FileProvider {
    private static String fileLocation;

    @Value(value = "${videoLocation}")
    public void setFileLocation(String videoLocation) {
        FileProvider.fileLocation = videoLocation;
    }

    public static String save(MultipartFile file,String type){

        try{
            //获取文件后缀
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            // 重构文件名称
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            String newFileName = pikId + "." + fileExt;
            String newFilePath="";
            if (type=="video"){
                //保存视频
                newFilePath="videos\\"+newFileName;
                File fileSave = new File(fileLocation, newFilePath);
                file.transferTo(fileSave);
            }else if (type=="cover"){
                //保存封面
                newFilePath="cover\\"+newFileName;
                File fileSave = new File(fileLocation, newFilePath);
                file.transferTo(fileSave);
            }
            return  newFilePath;

        }catch (Exception e){
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
