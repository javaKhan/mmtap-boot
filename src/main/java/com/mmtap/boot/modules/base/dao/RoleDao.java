package com.mmtap.boot.modules.base.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.base.entity.Role;

import java.util.List;

/**
 * 角色数据处理层
 * @author mmtap
 */
public interface RoleDao extends MmtapBootBaseDao<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
