package com.mmtap.boot.modules.base.service;

import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.modules.base.entity.QuartzJob;

import java.util.List;

/**
 * 定时任务接口
 * @author Exrick
 */
public interface QuartzJobService extends MmtapBootBaseService<QuartzJob,String> {

    /**
     * 通过类名获取
     * @param jobClassName
     * @return
     */
    List<QuartzJob> findByJobClassName(String jobClassName);
}