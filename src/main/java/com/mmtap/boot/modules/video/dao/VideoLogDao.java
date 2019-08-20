package com.mmtap.boot.modules.video.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.video.entity.VideoLog;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoLogDao extends MmtapBootBaseDao<VideoLog, String> {
}
