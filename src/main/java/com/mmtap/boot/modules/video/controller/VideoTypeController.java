package com.mmtap.boot.modules.video.controller;

import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.common.utils.PageUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.PageVo;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "视频体系管理接口")
@RequestMapping("/videoType")
@Transactional
public class VideoTypeController extends MmtapBootBaseController<VideoType, String> {

    @Autowired
    private VideoTypeService videoTypeService;


//    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
//    @ApiOperation(value = "视频类型列表")
//    public Result<Page<VideoType>> getByCondition(@ModelAttribute VideoType videoType,
//                                                  @ModelAttribute SearchVo searchVo,
//                                                  @ModelAttribute PageVo pageVo){
//
//        Page<VideoType> page = videoTypeService.findByCondition(videoType, searchVo, PageUtil.initPage(pageVo));
//        return new ResultUtil<Page<VideoType>>().setData(page);
//    }

    @Override
    public MmtapBootBaseService<VideoType, String> getService() {
        return null;
    }

    @PostMapping("/add")
    public Result addVideoType(VideoType vt){
        if (StringUtils.isEmpty(vt.getCode())|| StringUtils.isEmpty(vt.getName())){
            return new ResultUtil().setErrorMsg("分类参数不能为空");
        }
        videoTypeService.save(vt);
        return new ResultUtil().setSuccessMsg("添加成功");
    }

    @PostMapping("/edit")
    public Result editVideoType(VideoType vt){
        if (StringUtils.isEmpty(vt.getId())){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        if (StringUtils.isEmpty(vt.getCode())|| StringUtils.isEmpty(vt.getName())){
            return new ResultUtil().setErrorMsg("分类参数不能为空");
        }
        videoTypeService.save(vt);
        return new ResultUtil().setSuccessMsg("编辑完成");
    }

    @PostMapping("/del")
    public Result delVideoType(String id){
        if (StringUtils.isEmpty(id)){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        videoTypeService.delete(id);
        return new ResultUtil().setSuccessMsg("");
    }

    @PostMapping("/one")
    public Result oneVideoType(String id){
        if (StringUtils.isEmpty(id)){
            return new ResultUtil().setErrorMsg("参数不能为空");
        }
        Optional<VideoType> vt = videoTypeService.findByID(id);
        if (vt.isPresent()){
            return new ResultUtil().setData(vt.get(),"视频类型信息");
        }
        return new ResultUtil().setErrorMsg("未发现该类型信息");
    }

    @PostMapping("/list")
    public Result listVideoType(){
        List<VideoType> tl = videoTypeService.allType();
        Collections.sort(tl);
        List<Map> typeVsList = videoTypeService.countTypeVideo();
        List res = tl.stream().map(t->{
            long am = typeVsList.stream().filter(map -> t.getId().equals(map.get("type_id"))).count();
            t.setAmount(String.valueOf(am));
            return t;
        }).collect(Collectors.toList());
        return new ResultUtil().setData(res,"视频列表");
    }
}
