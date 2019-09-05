package com.mmtap.boot.modules.video.service;

import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.modules.video.entity.HisPo;
import com.mmtap.boot.modules.video.entity.VideoLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 视频体系接口
 * @author Mmtap
 */
public interface VideoLogService {
    Page getHistory(HisPo param, Pageable pageable);

    void saveVideoLog(VideoLog videoLog);
}