package com.mmtap.boot.modules.account.controller;

import cn.hutool.json.JSONUtil;
import com.mmtap.boot.base.MmtapBootBaseController;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.service.AccountService;
import com.mmtap.boot.modules.video.entity.TopVo;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mmtap
 */
@Slf4j
@RestController
@Api(description = "用户体系管理接口")
@RequestMapping("/account")
@Transactional
public class AccountController extends MmtapBootBaseController<Account, String> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private VideoTypeService videoTypeService;

    @Override
    public AccountService getService() {
        return accountService;
    }

//    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
//    @ApiOperation(value = "多条件分页获取")
//    public Result<Page<Account>> getByCondition(@ModelAttribute Account account,
//                                                @ModelAttribute SearchVo searchVo,
//                                                @ModelAttribute PageVo pageVo){
//
////        Page<Account> page = accountService.findByCondition(account, searchVo, PageUtil.initPage(pageVo));
//        return new ResultUtil<Page<Account>>().setData(null);
//    }

    /**
     * 前端登录
     * @param name
     * @return
     */
    @PostMapping("/login")
    public Result loginFront(String name){
        if (StringUtils.isEmpty(name)){
            return new ResultUtil().setErrorMsg("账户不正确");
        }
        Optional<Account> user = accountService.findUser(name,0);
        if (!user.isPresent()){
            return new ResultUtil().setErrorMsg("账户不正确!");
        }
        Map res = accountService.userLogin(user.get());
        return new ResultUtil().setData(res,"登录信息");
    }

    /**
     * 管理端登录
     * @param name
     * @param password
     * @return
     */
    @PostMapping("/login/admin")
    public Result loginBackend(String name,String password){
        if (StringUtils.isEmpty(name)|| StringUtils.isEmpty(password)){
            return new ResultUtil().setErrorMsg("账户或密码不正确");
        }
        Optional<Account> user = accountService.findUser(name,1);
        if (!user.isPresent()){
            return new ResultUtil().setErrorMsg("账户或密码不正确");
        }
        Map res = accountService.adminLogin(user.get(),password);
        return new ResultUtil().setData(res,"登录信息");
    }


    @PostMapping("/area")
    public Result area(){
        List areaLis = accountService.treeArea();
        return new ResultUtil().setData(areaLis);
    }

    /**
     * 添加用户
     * @return
     */
    @PostMapping("/add")
    public Result addUser(Account account, HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(account.getProvince())){
            return new ResultUtil().setErrorMsg("省份不能为空");
        }
        if (StringUtils.isEmpty(account.getSchoolID())){
            return new ResultUtil().setErrorMsg("学校ID不能为空");
        }
        if (StringUtils.isEmpty(account.getTeacher())){
            return new ResultUtil().setErrorMsg("老师不能为空");
        }
        accountService.saveAccount(account);
        return new ResultUtil().setSuccessMsg("账号添加成功");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @PostMapping("/del")
    public Result delUser(String id){
        if (StringUtils.isEmpty(id)){
            return new ResultUtil().setErrorMsg("删除用户参数为空!");
        }
        accountService.delete(id);
        return new ResultUtil().setSuccessMsg("删除成功");
    }

    /**
     * 编辑用户
     * @param account
     * @return
     */
    @PostMapping("/edit")
    public Result editUser(Account account){
        if (StringUtils.isEmpty(account.getId())){
            return new ResultUtil().setErrorMsg("账号ID不能为空");
        }
        if (StringUtils.isEmpty(account.getProvince())){
            return new ResultUtil().setErrorMsg("省份不能为空!");
        }
        if (StringUtils.isEmpty(account.getSchoolID())){
            return new ResultUtil().setErrorMsg("学校ID不能为空");
        }
        if (StringUtils.isEmpty(account.getTeacher())){
            return new ResultUtil().setErrorMsg("老师不能为空");
        }
        accountService.save(account);
        return new ResultUtil().setSuccessMsg("保存成功");
    }

    /**
     * 用户分页列表
     * @param pageable
     * @return
     */
    @PostMapping("/list")
    public Result listUser(Account account,Pageable pageable){
        Page<Account> page = accountService.listAccount(account,pageable);
        List<VideoType> vts = videoTypeService.allType();
        page.stream().forEach(a->{
            if (!StringUtils.isEmpty(a.getPerms())){
                List<String> ids = JSONUtil.parseArray(a.getPerms()).toList(String.class);
                List temp = vts.stream().filter(t->ids.contains(t.getId())).collect(Collectors.toList());
                a.setVts(temp);
            }
        });

        return new ResultUtil().setData(page,"账号列表");
    }

    /**
     * 管理端首页数据
     * @return
     */
    @PostMapping("/admin/info")
    public Result adminInfo(){
        Map map = new HashMap();
        map.put("userSum",0);
        map.put("videoSum",0);
        map.put("playSum",0);

        List vs = new ArrayList();
        vs.add(new TopVo("哪吒",3005));
        vs.add(new TopVo("狮子王",105));
        vs.add(new TopVo("无间道",12));
        vs.add(new TopVo("西游记",1));
        map.put("videoTop",vs);

        List ss = new ArrayList();
        ss.add(new TopVo("北京四中",23));
        ss.add(new TopVo("北京三中",15));
        ss.add(new TopVo("北京二中",12));
        ss.add(new TopVo("北京一中",0));
        map.put("schoolTop",ss);
        return new ResultUtil().setData(map);
    }


}