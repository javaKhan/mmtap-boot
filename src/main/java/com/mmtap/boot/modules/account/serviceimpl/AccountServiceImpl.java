package com.mmtap.boot.modules.account.serviceimpl;

import cn.hutool.core.date.DateUtil;
import com.mmtap.boot.common.constant.SecurityConstant;
import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.modules.account.dao.AccountDao;
import com.mmtap.boot.modules.account.dao.AreaDao;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.entity.Area;
import com.mmtap.boot.modules.account.service.AccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户体系接口实现
 * @author Mmtap
 */
@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AreaDao areaDao;

    @Override
    public AccountDao getRepository() {
        return accountDao;
    }

    @Async
    @CachePut(value = "area")
    public List initArea(){
        List<Area> areaList = areaDao.findAll();
        List<Area> items = areaList.stream().filter(area -> 0!=area.getPid()).collect(Collectors.toList());
        List<Area> root = areaList.stream().filter(area -> 0==area.getPid()).collect(Collectors.toList());
        root.forEach(item->findItemChildren(item,items));
        return root;
    }

    @Override
    public Optional<Account> findUser(String name,int role) {
       return accountDao.findByAccountAndRoleAndState(name,role,0);
    }

    /**
     * 前端登录
     * @param account
     * @return
     */
    @Override
    public Map userLogin(Account account) {
        Map res = new HashMap();
        //校验密码正确

        /*
            生成Token
            claimMap:token存储信息
            exp:过期时间
         */
        Map claimMap = new HashMap();
        claimMap.put("name",account.getTeacher());
        claimMap.put("uid",account.getId());
        claimMap.put("role",account.getRole());
        String token = JwtUtil.createToken(claimMap);
        /*
            返回前端信息
         */
        res.put("token",token);
        res.put("name",account.getTeacher());
        res.put("role",account.getRole());
        return res;
    }

    /**
     * 管理端登录
     * @param user
     * @param password
     * @return
     */
    @Override
    public Map adminLogin(Account user, String password) {
        Map res = new HashMap();
        //校验密码正确
        boolean adminCheck = false;
        /*
            生成Token
         */
        Map claimMap = new HashMap();
        claimMap.put("name",user.getAccount());
        claimMap.put("uid",user.getId());
        claimMap.put("role",user.getRole());
        String token = JwtUtil.createToken(claimMap);

        res.put("token",token);
        res.put("name",user.getAccount());
        res.put("role",user.getRole());
        return res;
    }

    @Override
    @Cacheable(value = "area")
    public List treeArea() {
        List<Area> areaList = areaDao.findAll();
        List<Area> root = areaList.stream().filter(area -> 0==area.getPid()).collect(Collectors.toList());
        List<Area> items = areaList.stream().filter(area -> 0!=area.getPid()).collect(Collectors.toList());
        root.forEach(item->findItemChildren(item,items));
        return root;
    }

    private void findItemChildren(Area item,List<Area> items){
        for (int j=0;j<items.size();j++){
            Area dt= items.get(j);
            if (item.getId()==dt.getPid()){
                item.getChild().add(dt);
                findItemChildren(dt,items);
            }
        }
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        Area area = areaDao.getOne(account.getProvince());
        account.setAccount(area.getNail()+account.getSchoolID());
        account.setProvince(1);
        account.setCity(2);
        account.setCounty(3);
        account.setPwd("test1");
        account.setMobile("13800138000");
        account.setRole(1);
        return save(account);
    }

    @Override
    public Page listAccount(Account account, Pageable pageable) {
       Page<Account> page = accountDao.findAll(new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return null;
            }
        },pageable);

        return page;
    }
}