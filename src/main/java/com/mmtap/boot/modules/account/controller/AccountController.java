package com.mmtap.boot.modules.account.controller;

import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.common.utils.PageUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.PageVo;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "用户体系管理接口")
@RequestMapping("/xboot/account")
@Transactional
public class AccountController extends MmtapBootBaseController<Account, String> {

    @Autowired
    private AccountService accountService;

    @Override
    public AccountService getService() {
        return accountService;
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取")
    public Result<Page<Account>> getByCondition(@ModelAttribute Account account,
                                                @ModelAttribute SearchVo searchVo,
                                                @ModelAttribute PageVo pageVo){

        Page<Account> page = accountService.findByCondition(account, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<Account>>().setData(page);
    }
}
