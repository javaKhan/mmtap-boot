package com.mmtap.boot.modules.account.service;

import com.mmtap.boot.base.MmtapBootBaseService;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.account.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户体系接口
 * @author Mmtap
 */
public interface AccountService extends MmtapBootBaseService<Account,String> {

//    /**
//    * 多条件分页获取
//    * @param account
//    * @param searchVo
//    * @param pageable
//    * @return
//    */
//    Page<Account> findByCondition(Account account, SearchVo searchVo, Pageable pageable);

    List initArea();

    Optional<Account> findUser(String name, int role);

    Map adminLogin(Account account,String password);

    Map userLogin(Account account);

    List treeArea();

    Account saveAccount(Account account);

    @Deprecated
    Page listAccount(Account account, Pageable pageable);

    Page pageAccount(Account account,Pageable pageable);

    List listArea(Integer id);
}