package com.mmtap.boot.modules.video.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 视频体系数据处理层
 * @author Mmtap
 */
public interface VideoDao extends MmtapBootBaseDao<Video,String> {
    @Query(nativeQuery = true,
            value = "select * from t_video where state=?1 and grade=?2 and type_id=?3 order by ordered ",
            countQuery = " select count(*) from t_video where state=?1 and grade=?2 and type_id=?3"
    )
    Page<Video> findByStateAndGradeAndType_id(String state, String grade, String type , Pageable pageable);

    @Query(nativeQuery = true,
    value = "select count(*) from t_video where vod is not null ")
    int videoSum();

    @Modifying
    @Query(nativeQuery = true,
    value = " UPDATE t_video  SET ordered=ordered+1 where ordered>=?1 and type_id=?2 ")
    void updateOrdered(Integer ordered, String type_id);

    @Query(nativeQuery = true,
    value = "select  * from t_video where type_id = ?1 limit 1 ")
    List<Video> findByType_id(String tid);
}