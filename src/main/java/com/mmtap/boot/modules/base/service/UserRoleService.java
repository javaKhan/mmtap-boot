package com.mmtap.boot.modules.base.service;


import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.modules.base.entity.UserRole;

import java.util.List;

/**
 * 用户角色接口
 * @author mmtap
 */
public interface UserRoleService extends MmtapBootBaseService<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
}
