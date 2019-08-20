package com.mmtap.boot.modules.video.service;

import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 视频体系接口
 * @author Mmtap
 */
public interface VideoService extends MmtapBootBaseService<Video,String> {

   Page listVideo(String grade,String typeID,String state,String word,Pageable pageable);

}