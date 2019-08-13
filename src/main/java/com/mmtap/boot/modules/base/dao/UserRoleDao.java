package com.mmtap.boot.modules.base.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.base.entity.UserRole;

import java.util.List;

/**
 * 用户角色数据处理层
 * @author mmtap
 */
public interface UserRoleDao extends MmtapBootBaseDao<UserRole,String> {

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
