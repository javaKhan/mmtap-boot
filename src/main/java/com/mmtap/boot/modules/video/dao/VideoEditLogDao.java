package com.mmtap.boot.modules.video.dao;

import com.mmtap.boot.modules.video.entity.VideoEditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoEditLogDao extends JpaRepository<VideoEditLog,String> {
    @Query(value = "select * from t_video_edit_log where vid=?1 order by create_time desc limit 5",nativeQuery = true)
    List<VideoEditLog> findTop5Log( String uid);
}
