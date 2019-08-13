package com.mmtap.boot.modules.base.service;


import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.base.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 * @author mmtap
 */
public interface LogService extends MmtapBootBaseService<Log,String> {

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
    /**
     * 删除所有
     */
    void deleteAll();
}
