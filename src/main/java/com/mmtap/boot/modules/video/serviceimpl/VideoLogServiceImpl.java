package com.mmtap.boot.modules.video.serviceimpl;

import com.mmtap.boot.modules.video.dao.VideoLogDao;
import com.mmtap.boot.modules.video.service.VideoLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视频体系接口实现
 * @author Mmtap
 */
@Slf4j
@Service
@Transactional
public class VideoLogServiceImpl implements VideoLogService {

    @Autowired
    private VideoLogDao videoLogDao;

    @Override
    public VideoLogDao getRepository() {
        return videoLogDao;
    }
}