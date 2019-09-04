package com.mmtap.boot.modules.video.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.video.entity.VideoType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 视频体系数据处理层
 * @author Mmtap
 */
public interface VideoTypeDao extends MmtapBootBaseDao<VideoType,String> {

    @Query(nativeQuery = true,
//    value = "select new Map(type_id,COUNT(*) as sl) from t_video GROUP BY type_id ")
    value = "select type_id,COUNT(*) as sl from t_video GROUP BY type_id ")
    List<Map> countTypeVideo();

    @Modifying
    @Query(nativeQuery = true,
    value = "update t_video_type set  ordered=ordered+1  where ordered>=?1 ")
    void updateOrder(Integer ordered);

    List<VideoType> findByName(String name);
}