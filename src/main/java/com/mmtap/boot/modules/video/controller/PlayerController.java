package com.mmtap.boot.modules.video.controller;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.video.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * 1：客户端先要拿到这个key才能进行客户端上传视频
     * @param vid
     * @param name
     * @return
     */
    @PostMapping("/update/key")
    public Result createUpdateVOD(String vid,String name){
        if (StringUtils.isEmpty(vid) || StringUtils.isEmpty(name)){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        CreateUploadVideoResponse response = null;
        try {
            response = playerService.createUploadVideo(vid,name);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil().setErrorMsg("视频服务异常");
        }
        return new ResultUtil().setData(response);
    }

    /**
     *
     * @return
     */
    @PostMapping("/play/key")
    public Result getPlayKey(String vid){
        if (StringUtils.isEmpty(vid)){
            return new  ResultUtil().setErrorMsg("参数不能为空");
        }
        GetVideoPlayAuthResponse res = null;
        try {
            res = playerService.getVideoPlayAuth(vid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil().setErrorMsg("视频服务异常");
        }
        return new ResultUtil().setData(res);
    }

}
