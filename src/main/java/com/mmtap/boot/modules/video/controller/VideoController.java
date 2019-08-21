package com.mmtap.boot.modules.video.controller;

import cn.hutool.core.io.FileUtil;
import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.common.constant.CommonConstant;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.VideoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "视频体系管理接口")
@RequestMapping("/video")
@Transactional
public class VideoController  {

    @Autowired
    private VideoService videoService;
//
//    @Override
//    public VideoService getService() {
//        return videoService;
//    }
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
     * 视频添加前置ID获取
     * @return
     */
    @PostMapping("/add/pre")
    public Result preVideoAdd(){
        return new ResultUtil().setData(new Video());
    }

    /**
     * 上传缩略图
     * @param vid
     * @param pic
     * @return
     */
    @PostMapping("/pic/upload")
    public Result videoPic(String vid, MultipartFile pic) throws IOException {
        if (StringUtils.isEmpty(vid)){
            return new ResultUtil().setErrorMsg("ID不能为空");
        }
        if (ObjectUtils.isEmpty(pic)|| pic.isEmpty()){
            return new ResultUtil().setErrorMsg("文件不能为空");
        }
        String BASE_PATH = System.getProperty("user.dir")+ CommonConstant.PIC_PATH;
        String ext = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf("."));
        if (!".png.jpg.jpeg".contains(ext.toLowerCase())){
            return new ResultUtil().setErrorMsg("文件类型异常");
        }
        File dest = FileUtil.touch(BASE_PATH+"/"+vid); //123213124534534534.png
        pic.transferTo(dest);
        return new ResultUtil().setData("上传成功!");
    }

    /**
     * 图片显示
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/pic/{id}")
    public void picResource(@PathVariable String id, HttpServletResponse response) throws IOException {
        String BASE_PATH = System.getProperty("user.dir")+ CommonConstant.PIC_PATH;
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        File pic = new File(BASE_PATH+"/"+id);
        if (pic.exists()){
            InputStream is = FileUtil.getInputStream(BASE_PATH+"/"+id);
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = is.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();
            out.close();
        }else {
            response.setStatus(404);
        }
    }

    /**
     * 保存视频
     * @param video
     * @return
     */
    @PostMapping("/save")
    public Result videoSave(Video video){
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

//    /**
//     * 视频编辑
//     * @param video
//     * @return
//     */
//    @PostMapping("/edit")
//    public Result videoEdit(Video video){
//        if (StringUtils.isEmpty(video.getType_id())){
//            return new ResultUtil().setErrorMsg("缺少类型参数!");
//        }
//        if (StringUtils.isEmpty(video.getGrade())){
//            return new ResultUtil().setErrorMsg("缺少年级!");
//        }
//        if (StringUtils.isEmpty(video.getName())){
//            return new ResultUtil().setErrorMsg("缺少名称参数!");
//        }
//        if (StringUtils.isEmpty(video.getState())){
//            return new ResultUtil().setErrorMsg("缺少状态参数!");
//        }
//        Video nv = videoService.save(video);
//        return new ResultUtil().setData(nv);
//    }

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
        Video ov = videoService.get(vid);
        ov.setState(state);
        Video nv = videoService.save(ov);
        return new ResultUtil().setData(nv,"编辑成功");
    }


}
