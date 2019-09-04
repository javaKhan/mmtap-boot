package com.mmtap.boot.modules.account.controller;

import cn.hutool.json.JSONUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.common.utils.ResultUtil;
import com.mmtap.boot.common.vo.Result;
import com.mmtap.boot.modules.account.dao.AreaDao;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.entity.AccountListVo;
import com.mmtap.boot.modules.account.entity.Area;
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
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Autowired
    private AreaDao areaDao;

    /**
     * 前端登录
     * @param name
     * @return
     */
    @CrossOrigin
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
    @CrossOrigin
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
            return new ResultUtil().setErrorMsg("老师不能为空!");
        }
        //判断用户是否存在
        Area area = areaDao.getOne(account.getProvince());
        String accStr = area.getNail()+account.getSchoolID();
        Optional haveAcc = accountService.findUser(accStr,0);
        if (haveAcc.isPresent()){
            return new ResultUtil().setErrorMsg("账号已经存在!");
        }
        Account res = accountService.saveAccount(account,accStr);
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
            return new ResultUtil().setErrorMsg("学校ID不能为空!");
        }
        if (StringUtils.isEmpty(account.getTeacher())){
            return new ResultUtil().setErrorMsg("老师不能为空");
        }
        //校验账号存在
        Area area = areaDao.getOne(account.getProvince());
        //生成账号形式
        String accStr = area.getNail()+account.getSchoolID();
        if (!accStr.equals(account.getAccount())){
            Optional haveAcc = accountService.findUser(accStr,0);
            if (haveAcc.isPresent()){
                return new ResultUtil().setErrorMsg("账号已经存在!");
            }
        }
        accountService.saveAccount(account, accStr);
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

        List vs = playerService.topVideo();
        map.put("videoTop",vs);

        List ss = playerService.topSchool();
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
        String token = request.getHeader(SecurityConstant.HEADER);
        String uid = JwtUtil.getHeaderValue(token,"uid");
        Optional<Account> account = accountService.findByUID(uid);

        if (account.isPresent() && account.get().getRole()==1){
            if (!opd.equals(account.get().getPwd())){
                return new ResultUtil().setSuccessMsg("原密码错误!");
            }

            account.get().setPwd(npd);
            accountService.saveAdminAccount(account.get());
            return new ResultUtil().setSuccessMsg("修改成功");
        }else {
            return new ResultUtil().setErrorMsg("无此权限");
        }
    }


}
