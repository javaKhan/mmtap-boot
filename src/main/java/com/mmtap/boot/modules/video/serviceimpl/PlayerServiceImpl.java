package com.mmtap.boot.modules.video.serviceimpl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.mmtap.boot.modules.video.service.PlayerService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private DefaultAcsClient client;

    public CreateUploadVideoResponse createUploadVideo(String vid,String name) throws Exception {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle("this is a sample");  //标题
        request.setFileName("filename.mp4");    //文件名
        JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", "http://xxxxx");  //回调地址
        messageCallback.put("CallbackType", "http");         //回调协议
        userData.put("MessageCallback", messageCallback.toJSONString());
        JSONObject extend = new JSONObject();
        extend.put("vid", "user-defined-id");  //业务ID
        userData.put("Extend", extend.toJSONString());
        request.setUserData(userData.toJSONString());
        return client.getAcsResponse(request);
    }

    /**
     * 获取视频播放地址
     * @param vid
     * @return
     * @throws Exception
     */
    public GetPlayInfoResponse getPlayInfo(String vid) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(vid); //"视频ID"
        return client.getAcsResponse(request);
    }

    /**
     * 获取视频播放凭证
     * @param vid
     * @return
     * @throws Exception
     */
    public GetVideoPlayAuthResponse getVideoPlayAuth(String vid) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(vid); //"视频ID"
        return client.getAcsResponse(request);
    }
}
