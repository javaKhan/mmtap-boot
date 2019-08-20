package com.mmtap.boot.modules.account.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.account.entity.Account;

import java.util.Optional;

/**
 * 用户体系数据处理层
 * @author Mmtap
 */
public interface AccountDao extends MmtapBootBaseDao<Account,String> {

    Optional<Account> findByAccountAndRoleAndState(String name, int role, int state);
}