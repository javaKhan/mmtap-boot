package com.mmtap.boot.modules.video.service;

import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.entity.VideoType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 视频体系接口
 * @author Mmtap
 */
public interface VideoTypeService extends MmtapBootBaseService<VideoType,String> {

//    /**
//    * 多条件分页获取
//    * @param videoType
//    * @param searchVo
//    * @param pageable
//    * @return
//    */
//    Page<VideoType> findByCondition(VideoType videoType, SearchVo searchVo, Pageable pageable);

    List<VideoType> allType();

    Optional<VideoType> findByID(String id);

    List countTypeVideo();

    void saveVideoType(VideoType vt);

    List<VideoType> findByVideoName(String name);

    List findUserVideoType(List<String> tids);
}