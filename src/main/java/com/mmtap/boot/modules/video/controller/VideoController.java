package com.mmtap.boot.modules.video.controller;

import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.common.utils.PageUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.PageVo;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "视频体系管理接口")
@RequestMapping("/xboot/video")
@Transactional
public class VideoController extends MmtapBootBaseController<Video, String> {

    @Autowired
    private VideoService videoService;

    @Override
    public VideoService getService() {
        return videoService;
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Result<Page<Video>> getByCondition(@ModelAttribute Video video,
                                              @ModelAttribute SearchVo searchVo,
                                              @ModelAttribute PageVo pageVo){

        Page<Video> page = videoService.findByCondition(video, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<Video>>().setData(page);
    }
}
