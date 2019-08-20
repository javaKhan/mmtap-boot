package com.mmtap.boot.modules.video.controller;

import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.VideoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "视频体系管理接口")
@RequestMapping("/video")
@Transactional
public class VideoController extends MmtapBootBaseController<Video, String> {

    @Autowired
    private VideoService videoService;
//
    @Override
    public VideoService getService() {
        return videoService;
    }
//


    /**
     * 视频列表
     * @param grade
     * @param typeID
     * @param state
     * @param word
     * @param pageable
     * @return
     */
    @PostMapping("/list")
    public Result videoList(String grade, String typeID, String state, String word, Pageable pageable){
        Page page = videoService.listVideo(grade,typeID,state,word,pageable);
        return new ResultUtil().setData(page);
    }

    /**
     * 添加视频
     * @param video
     * @return
     */
    @PostMapping("/add")
    public Result videoAdd(Video video){
        if (StringUtils.isEmpty(video.getType_id())){
            return new ResultUtil().setErrorMsg("缺少类型参数");
        }
        if (StringUtils.isEmpty(video.getGrade())){
            return new ResultUtil().setErrorMsg("缺少年级");
        }
        if (StringUtils.isEmpty(video.getName())){
            return new ResultUtil().setErrorMsg("缺少名称参数");
        }
        if (StringUtils.isEmpty(video.getState())){
            return new ResultUtil().setErrorMsg("缺少状态参数");
        }
        Video nv = videoService.save(video);
        return new ResultUtil().setData(nv);
    }

    /**
     * 视频详情
     * @param vid
     * @return
     */
    @PostMapping("/one")
    public Result videoInfo(String vid){
        Video video = videoService.get(vid);
        return new ResultUtil().setData(video);
    }

    /**
     * 视频编辑
     * @param video
     * @return
     */
    @PostMapping("/edit")
    public Result videoEdit(Video video){
        Video nv = videoService.save(video);
        return new ResultUtil().setData(nv);
    }

    /**
     * 视频的发布或取消
     * @param vid
     * @param state
     * @return
     */
    @PostMapping("/state/edit")
    public Result videoStateEdit(String vid,String state){
        if (StringUtils.isEmpty(vid) || StringUtils.isEmpty(state)){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        return new ResultUtil().setSuccessMsg("编辑成功");
    }


}
