package com.mmtap.boot.modules.video.controller;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.service.AccountService;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.entity.VideoLog;
import com.mmtap.boot.modules.video.service.PlayerService;
import com.mmtap.boot.modules.video.service.VideoLogService;
import com.mmtap.boot.modules.video.service.VideoService;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private VideoTypeService videoTypeService;

    @Autowired
    private VideoLogService videoLogService;

    /**
     * 1：客户端先要拿到这个key才能进行客户端上传视频
     * @param vid
     * @param name
     * @return
     */
    @PostMapping("/key/video")
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
     * 获取图片上传授权
     * @param id
     * @return
     */
    @PostMapping("/key/img")
    public Result createKeyImg(String id){
        if (StringUtils.isEmpty(id)){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        CreateUploadImageResponse response = null;
        try {
            response = playerService.createUploadImage(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil().setErrorMsg("云服务异常");
        }
        return new ResultUtil().setData(response);
    }

    /**
     *
     * @return
     */
    @PostMapping("/play/key")
    public Result getPlayKey(String vid, HttpServletRequest request){
        if (StringUtils.isEmpty(vid)){
            return new  ResultUtil().setErrorMsg("参数不能为空");
        }
        String token = request.getHeader(SecurityConstant.HEADER);
        String uid = JwtUtil.getHeaderValue(token,"uid");
        Optional<Video> vo = videoService.findByVid(vid);
        Map resMap = new HashMap();
        if (vo.isPresent()){
            try {
                String vod = vo.get().getVod();
                if (StringUtils.isEmpty(vod)){
                    return new ResultUtil().setErrorMsg("该课程还未上传");
                }
                GetVideoPlayAuthResponse res = playerService.getVideoPlayAuth(vod);
                log.info(res.toString());
                resMap.put("playAuth",res);
                resMap.put("title",vo.get().getName());
                //记录播放日志
                VideoLog videoLog = new VideoLog();
                videoLog.setVid(vid);
                videoLog.setUid(uid);
                videoLog.setVod(vod);
                videoLogService.save(videoLog);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultUtil().setErrorMsg("视频服务异常");
            }
        }
        return new ResultUtil().setData(resMap);
    }



    @PostMapping("/play/type")
    public Result getType(HttpServletRequest request){
        String token = request.getHeader(SecurityConstant.HEADER);
        String uid = JwtUtil.getHeaderValue(token,"uid");
        Optional<Account> accountOptional = accountService.findByUID(uid);
        if (accountOptional.isPresent() && !StringUtils.isEmpty(accountOptional.get().getPerms())){
            List<String> tids = JSONUtil.parseArray(accountOptional.get().getPerms()).toList(String.class);
            List res = videoTypeService.findUserVideoType(tids);
            return new ResultUtil().setData(res,"视频信息");
        }
        return new ResultUtil().setData(null,"视频信息");
    }

    @PostMapping("/play/list")
    public Result getVideo(String grade, String typdID, Pageable pageable,HttpServletRequest request){
        if (StringUtils.isEmpty(typdID)){
            return new ResultUtil().setErrorMsg("缺少类别");
        }
        if (StringUtils.isEmpty(grade)){
            return new ResultUtil().setErrorMsg("缺少年级");
        }
        Object res = null;
        try {
            String token = request.getHeader(SecurityConstant.HEADER);
            String uid = JwtUtil.getHeaderValue(token,"uid");
            res = playerService.getVideoList(uid,grade,typdID,pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultUtil().setErrorMsg(e.getMessage());
        }
        return new ResultUtil().setData(res,"视频信息");

    }

}
