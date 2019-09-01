package com.mmtap.boot.modules.video.serviceimpl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class VODClient {
    @Value("${VOD.regionId}")
    private String regionId;
    @Value("${VOD.accessKeyId}")
    private String accessKeyId;
    @Value("${VOD.accessKeySecret}")
    private String accessKeySecret;


    @Bean
    public DefaultAcsClient initVodClient() throws ClientException {
        if (StringUtils.isEmpty(regionId)){
         regionId = "cn-beijing";  // 点播服务接入区域
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
