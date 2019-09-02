package com.mmtap.boot.modules.video.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.video.entity.TopVo;
import com.mmtap.boot.modules.video.entity.VideoLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoLogDao extends MmtapBootBaseDao<VideoLog, String> {
    @Query(nativeQuery = true,
    value = "select count(*) from t_video_log ")
    int playSum();

    @Query(nativeQuery = true,
    value = "SELECT NAME,sl FROM (SELECT vid,count(*) AS sl FROM t_video_log WHERE uid IS NOT NULL AND vid IS NOT NULL GROUP BY vid ORDER BY 2 DESC LIMIT 10) l LEFT JOIN t_video v ON v.id=l.vid where v.name is not null ")
    List topVideo();

    @Query(nativeQuery = true,
    value = " SELECT school_name,sl FROM (\n" +
            "SELECT uid,count(*) AS sl FROM t_video_log WHERE uid IS NOT NULL AND vid IS NOT NULL GROUP BY uid ORDER BY 2 DESC LIMIT 10) l LEFT JOIN t_account a ON l.uid=a.id WHERE school_name IS NOT NULL ")
    List topSchool();

    @Query(nativeQuery = true,
    value = "select vid,count(*) sl from t_video_log where vid in ?1 GROUP BY vid ")
    List sumPageVideoPlay(List<String> vids);
}
