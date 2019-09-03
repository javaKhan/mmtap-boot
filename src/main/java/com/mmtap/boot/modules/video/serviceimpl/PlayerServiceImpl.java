package com.mmtap.boot.modules.video.serviceimpl;

import cn.hutool.json.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.mmtap.boot.modules.video.dao.VideoDao;
import com.mmtap.boot.modules.video.dao.VideoLogDao;
import com.mmtap.boot.modules.video.entity.TopVo;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
        userData.put("MessageCallback", messageCallback.toString());
        JSONObject extend = new JSONObject();
        extend.put("vid", vid);  //业务ID
        userData.put("Extend", extend.toString());
        request.setUserData(userData.toString() );
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
        userData.put("MessageCallback", messageCallback.toString());
        JSONObject extend = new JSONObject();
        userData.put("Extend", extend.toString());
        extend.put("bid", id);
        request.setUserData(userData.toString());
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
        String vs = page.stream().filter(video -> !StringUtils.isEmpty(video.getVod())).map(v->v.getVod()).collect(Collectors.joining(","));

        //设置视频信息
        if (!StringUtils.isEmpty(vs)){
            GetVideoInfosRequest request = new GetVideoInfosRequest();
            request.setVideoIds(vs);
            GetVideoInfosResponse response = client.getAcsResponse(request);
            List<GetVideoInfosResponse.Video> vl = response.getVideoList();
            page.stream().forEach(video -> {
                for (int i=0;i<vl.size();i++){
                   GetVideoInfosResponse.Video  v =  vl.get(i);
                    if (video.getVod().equals(v.getVideoId())){
                        video.setCoverURL(v.getCoverURL());
                    }
                }
            });
        }

        //设置播放数量
        List<String> vids = page.stream().map(video -> video.getId()).collect(Collectors.toList());
        if (vids.size()>0){
            List pl = videoLogDao.sumPageVideoPlay(vids);
            page.stream().forEach(video -> {
                //设置播放数量
                for (int i=0;null!= pl && i<pl.size();i++){
                    Object[] o = (Object[]) pl.get(i);
                    if (video.getId().equals(o[0].toString())){
                        video.setPlaySum(Integer.parseInt(o[1].toString()));
                    }
                }
                if (ObjectUtils.isEmpty(video.getPlaySum())){
                    video.setPlaySum(0);
                }
            });
        }


        //设置图片
        page.forEach(video -> {

            if (!StringUtils.isEmpty(video.getImg())){
                GetImageInfoRequest request = new GetImageInfoRequest();
                request.setImageId(video.getImg());
                GetImageInfoResponse response = null;
                try {
                    response = client.getAcsResponse(request);
                    video.setImgURL(response.getImageInfo().getURL());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
        });

        return page;
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
