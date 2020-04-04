package com.example.xiaoying.provider;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: codeape
 * @Date: 2020/3/31 23:40
 * @Version: 1.0
 */
@Component
public class UUIdProvider {
    public static String getAccountIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        return machineId + String.format("%015d", hashCodeV);
    }
}
