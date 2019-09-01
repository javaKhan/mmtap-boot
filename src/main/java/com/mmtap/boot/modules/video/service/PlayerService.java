package com.mmtap.boot.modules.video.service;

import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PlayerService {

    CreateUploadVideoResponse createUploadVideo(String vid, String name) throws Exception ;

    GetPlayInfoResponse getPlayInfo(String vid) throws Exception;

    GetVideoPlayAuthResponse getVideoPlayAuth(String vid) throws Exception;

    CreateUploadImageResponse createUploadImage(String id) throws Exception;

    Page getVideoList(String grade, String typdID, Pageable pageable) throws Exception;
}
