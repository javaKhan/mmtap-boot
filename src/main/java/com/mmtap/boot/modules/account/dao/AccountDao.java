package com.mmtap.boot.modules.account.dao;

import com.mmtap.boot.base.MmtapBootBaseDao;
import com.mmtap.boot.modules.account.entity.Account;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 用户体系数据处理层
 * @author Mmtap
 */
public interface AccountDao extends MmtapBootBaseDao<Account,String> {

    Optional<Account> findByAccountAndRoleAndState(String name, int role, int state);

    @Query(value = "select count(*) from t_account where state=0 and role=0 ",
    nativeQuery = true)
    int userSum();
}