package com.mmtap.boot.modules.video.serviceimpl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.mmtap.boot.modules.video.dao.VideoDao;
import com.mmtap.boot.modules.video.dao.VideoLogDao;
import com.mmtap.boot.modules.video.entity.TopVo;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private DefaultAcsClient client;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private VideoLogDao videoLogDao;

    public CreateUploadVideoResponse createUploadVideo(String vid,String name) throws Exception {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(name);  //标题
        request.setFileName(name);    //文件名
        JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", "http://dev.mmtap.com/"+vid);  //回调地址
        messageCallback.put("CallbackType", "http");         //回调协议
        userData.put("MessageCallback", messageCallback.toJSONString());
        JSONObject extend = new JSONObject();
        extend.put("vid", vid);  //业务ID
        userData.put("Extend", extend.toJSONString());
        request.setUserData(userData.toJSONString() );
        return client.getAcsResponse(request);
    }

    public CreateUploadImageResponse createUploadImage(String id) throws Exception {
        CreateUploadImageRequest request = new CreateUploadImageRequest();
        request.setImageType("default");
//        request.setImageExt("gif");
//        request.setTitle("id-img");
        JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackURL", "http://dev.mmtap.com/id");
        messageCallback.put("CallbackType", "http");
        userData.put("MessageCallback", messageCallback.toJSONString());
        JSONObject extend = new JSONObject();
        userData.put("Extend", extend.toJSONString());
        extend.put("bid", id);
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

    @Override
    public Page getVideoList(String grade, String typdID, Pageable pageable) throws Exception{
        Page<Video> page = videoDao.findByStateAndGradeAndType_id("1",grade,typdID,pageable);
        //补充阿里信息
        GetVideoInfosRequest request = new GetVideoInfosRequest();
        String vs = page.stream().filter(video -> !StringUtils.isEmpty(video.getVod())).map(v->v.getVod()).collect(Collectors.joining());
        if (!StringUtils.isEmpty(vs)){
            request.setVideoIds(vs);
            GetVideoInfosResponse response = client.getAcsResponse(request);
            List vl = response.getVideoList();
            log.info(vl.toString());
            return page;
        }else {
            return null;
        }
    }

    @Override
    public int playSum() {
        return videoLogDao.playSum();
    }

    @Override
    public List topVideo() {
        List res  = videoLogDao.topVideo();
        return formatVo(res);
    }

    @Override
    public List topSchool() {
        List res  = videoLogDao.topSchool();
        return formatVo(res);
    }

    private List<TopVo> formatVo(List data){
        List temp = new ArrayList();;
        for (int i=0;i<data.size();i++){
            Object[] v =(Object[]) data.get(i);
            TopVo vo = new TopVo();
            vo.setName(v[0].toString());
            vo.setSum(Integer.parseInt(v[1].toString()));
            temp.add(vo);
        }
        return temp;
    }
}
