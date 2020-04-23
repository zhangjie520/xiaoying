package com.example.xiaoying.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.BucketAuthorization;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileBucketLocalAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.example.xiaoying.exception.CustomizeErrorCode;
import com.example.xiaoying.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: codeape
 * @Date: 2020/4/18 21:20
 * @Version: 1.0
 */
@Component
public class UcloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;
    @Value("${ucloud.ufile.region}")
    private String region;
    @Value("${ucloud.ufile.expires-duration}")
    private Integer expiresDuration;
    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    public String upload(InputStream fileStream, String mimeType,String fileName){
        // 对象相关API的授权器
        ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
                publicKey, privateKey);
        // 对象操作需要ObjectConfig来配置您的地区和域名后缀
        ObjectConfig config = new ObjectConfig(region, suffix);

        String[] filePaths = fileName.split("\\.");
        String generatedFileName;
        if (filePaths.length>1){
            generatedFileName=UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
            if (response!=null && response.getRetCode()==0){
                //  5 * 60秒 --> 5分钟后过期
                String url = UfileClient.object(OBJECT_AUTHORIZER, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expiresDuration)
                        .createUrl();
                return url;
            }
        } catch (UfileClientException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        return "";
    }
}
