package com.mmtap.boot.modules.account.controller;

import cn.hutool.json.JSONUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.entity.AccountListVo;
import com.mmtap.boot.modules.account.service.AccountService;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.PlayerService;
import com.mmtap.boot.modules.video.service.VideoService;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PlayerService playerService;
    @Autowired
    private VideoTypeService videoTypeService;

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


    @RequestMapping("/area")
    public Result area(){
        List areaLis = accountService.treeArea();
        return new ResultUtil().setData(areaLis);
    }

    @PostMapping("/city")
    public Result areaFetch(Integer id){
        if (ObjectUtils.isEmpty(id)){
            id =0 ;
        }
        List areaLis = accountService.listArea(id);
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
        Account res = accountService.saveAccount(account);
        return new ResultUtil().setData(res.getAccount(),"账号添加成功");
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
     * 用户详情
     * @param id
     * @return
     */
    @PostMapping("/one")
    public Result userDetail(String id){
        if (StringUtils.isEmpty(id)){
            return new ResultUtil().setErrorMsg("用户参数为空!");
        }
        Account u = accountService.get(id);
        List<VideoType> vts = videoTypeService.allType();
        List<String> ids = JSONUtil.parseArray(u.getPerms()).toList(String.class);
        List temp = vts.stream().filter(t->ids.contains(t.getId())).collect(Collectors.toList());
        u.setVts(temp);
        return new ResultUtil().setData(u,"成功");
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
        accountService.saveAccount(account);
        return new ResultUtil().setSuccessMsg("保存成功");
    }

    /**
     * 用户分页列表
     * @param pageable
     * @return
     */
    @PostMapping("/list")
    public Result listUser(Account account,Pageable pageable){
//        Page<Account> page = accountService.listAccount(account,pageable);
        Page<AccountListVo> page = accountService.pageAccount(account,pageable);
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

        int userSum = accountService.userSum();
        map.put("userSum",userSum);

        int videoSum = videoService.videoSum();
        map.put("videoSum",videoSum);

        int playSum = playerService.playSum();
        map.put("playSum",playSum);

        List vs = new ArrayList();
        vs = playerService.topVideo();
//        vs.add(new TopVo("哪吒",3005));
//        vs.add(new TopVo("狮子王",105));
//        vs.add(new TopVo("无间道",12));
//        vs.add(new TopVo("西游记",1));
        map.put("videoTop",vs);

        List ss = new ArrayList();
        ss = playerService.topSchool();
//        ss.add(new TopVo("北京四中",23));
//        ss.add(new TopVo("北京三中",15));
//        ss.add(new TopVo("北京二中",12));
//        ss.add(new TopVo("北京一中",0));
        map.put("schoolTop",ss);
        return new ResultUtil().setData(map);
    }

    /**
     * 管理：修改密码接口
     * @param opd
     * @param npd
     * @param request
     * @return
     */
    @PostMapping("/admin/reset")
    public Result adminReset(String opd,String npd,HttpServletRequest request){
        if (StringUtils.isEmpty(opd)){
            return new ResultUtil().setErrorMsg("原密码不能为空");
        }
        if (StringUtils.isEmpty(StringUtils.isEmpty(npd))){
            return new ResultUtil().setErrorMsg("密码不能为空");
        }
        String token = request.getHeader("token");
        String name = "test";
        Optional<Account> account = accountService.findUser(name,1);
        if (account.isPresent()){
            account.get().setPwd(npd);
            accountService.saveAccount(account.get());
            return new ResultUtil().setSuccessMsg("修改成功");
        }else {
            return new ResultUtil().setErrorMsg("无此权限");
        }
    }


}
