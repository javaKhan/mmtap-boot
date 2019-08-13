package com.mmtap.boot.modules.video.controller;

import MmtapBootBaseController;
import PageUtil;
import ResultUtil;
import PageVo;
import Result;
import SearchVo;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "视频体系管理接口")
@RequestMapping("/xboot/videoType")
@Transactional
public class VideoTypeController extends MmtapBootBaseController<VideoType, String> {

    @Autowired
    private VideoTypeService videoTypeService;

    @Override
    public VideoTypeService getService() {
        return videoTypeService;
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Result<Page<VideoType>> getByCondition(@ModelAttribute VideoType videoType,
                                                            @ModelAttribute SearchVo searchVo,
                                                            @ModelAttribute PageVo pageVo){

        Page<VideoType> page = videoTypeService.findByCondition(videoType, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<VideoType>>().setData(page);
    }
}
