package com.mmtap.boot.modules.base.service;


import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.modules.base.entity.Role;

import java.util.List;

/**
 * 角色接口
 * @author mmtap
 */
public interface RoleService extends MmtapBootBaseService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
