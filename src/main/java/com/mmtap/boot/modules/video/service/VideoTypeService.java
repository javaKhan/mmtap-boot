package com.mmtap.boot.modules.video.service;

import MmtapBootBaseService;
import com.mmtap.boot.modules.video.entity.VideoType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import SearchVo;

import java.util.List;

/**
 * 视频体系接口
 * @author Mmtap
 */
public interface VideoTypeService extends MmtapBootBaseService<VideoType,String> {

    /**
    * 多条件分页获取
    * @param videoType
    * @param searchVo
    * @param pageable
    * @return
    */
    Page<VideoType> findByCondition(VideoType videoType, SearchVo searchVo, Pageable pageable);
}