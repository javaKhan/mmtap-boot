package com.mmtap.boot.modules.account.serviceimpl;

import com.mmtap.boot.common.utils.JwtUtil;
import com.mmtap.boot.modules.account.dao.AccountDao;
import com.mmtap.boot.modules.account.dao.AreaDao;
import com.mmtap.boot.modules.account.entity.Account;
import com.mmtap.boot.modules.account.entity.AccountListVo;
import com.mmtap.boot.modules.account.entity.Area;
import com.mmtap.boot.modules.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
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

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public Optional<Account> findByUID(String uid) {
        return accountDao.findById(uid);
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

//    private void findItemChildren(Area item,List<Area> items){
//        for (int j=0;j<items.size();j++){
//            Area dt= items.get(j);
//            if (item.getId()==dt.getPid()){
//                item.getChild().add(dt);
//                findItemChildren(dt,items);
//            }
//        }
//    }

    private void findItemChildren(Area item,List<Area> items){
        List<Area> child = items.stream().filter(a->item.getId()==a.getPid()).collect(Collectors.toList());
        item.setChild(child);
        if (child.size()>0){
            for (Area a : item.getChild()){
                findItemChildren(a,items);
            }
        }

//        for (int j=0;j<items.size();j++){
//            Area dt= items.get(j);
//            if (item.getId()==dt.getPid()){
//                item.getChild().add(dt);
//                findItemChildren(dt,items);
//            }
//        }
    }


    @Override
    @Transactional
    public Account saveAccount(Account account, String accStr) {
        account.setAccount(accStr);
        account.setRole(0);
        account.setState(0);
        return accountDao.save(account);
    }

    @Transactional
    public Account saveAdminAccount(Account account){
        return accountDao.save(account);
    }

    @Override
    @Deprecated
    public Page listAccount(Account account, Pageable pageable) {
       Page<Account> page = accountDao.findAll(new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> pl = new ArrayList();
                if (!ObjectUtils.isEmpty(account.getProvince())){
                    pl.add(criteriaBuilder.equal(root.get("province"),account.getProvince()));
                }
                if (!ObjectUtils.isEmpty(account.getCity())){
                    pl.add(criteriaBuilder.equal(root.get("city"),account.getCity()));
                }
                if (!ObjectUtils.isEmpty(account.getCounty())){
                    pl.add(criteriaBuilder.equal(root.get("county"),account.getCounty()));
                }
                if (!StringUtils.isEmpty(account.getSchoolID())){
                    pl.add(criteriaBuilder.like(root.get("schoolID"),"%"+account.getSchoolID()+"%"));
                }
                if (!StringUtils.isEmpty(account.getSchoolName())){
                    pl.add(criteriaBuilder.like(root.get("schoolName"),"%"+account.getSchoolName()+"%"));
                }
                if (!StringUtils.isEmpty(account.getTeacher())){
                    pl.add(criteriaBuilder.like(root.get("teacher"),"%"+account.getTeacher()+"%"));
                }
                if (!StringUtils.isEmpty(account.getState())){
                    pl.add(criteriaBuilder.equal(root.get("state"),account.getState()));
                }
                if (!StringUtils.isEmpty(account.getMobile())){
                    pl.add(criteriaBuilder.like(root.get("teacher"),"%"+account.getMobile()+"%"));
                }
                pl.add(criteriaBuilder.equal(root.get("role"),0));

                Predicate[] predicates = new Predicate[pl.size()];
                criteriaQuery.where(pl.toArray(predicates));
                return null;
            }
        },pageable);

        return page;
    }

    public Page pageAccount(Account account,Pageable pageable){
        String sql = "select u.id,u.schoolid,u.school_name,CONCAT(a.name,b.name,c.name) as local,teacher,mobile,state,perms from t_account u LEFT JOIN area a ON u.province=a.id LEFT JOIN area b on u.city=b.id LEFT JOIN area c on u.county=c.id where role=0 ";
        String countSql = " select count(*) from t_account where role=0 ";
        if (!ObjectUtils.isEmpty(account.getProvince())){
            sql = sql+" and province="+account.getProvince();
            countSql = countSql+" and province="+account.getProvince();
        }
        if (!ObjectUtils.isEmpty(account.getCity())){
            sql = sql+" and city="+account.getCity();
            countSql = countSql+" and city="+account.getCity();
        }
        if (!ObjectUtils.isEmpty(account.getCounty())){
            sql = sql+" and county="+account.getCounty();
            countSql = countSql+" and county="+account.getCounty();
        }
        if (!StringUtils.isEmpty(account.getSchoolID())){
            sql = sql+" and schoolid like '%"+account.getSchoolID()+"%' ";
            countSql = countSql+" and schoolid like '%"+account.getSchoolID()+"%' ";
        }
        if (!StringUtils.isEmpty(account.getSchoolName())){
            sql = sql+" and school_name like '%"+account.getSchoolName()+"%'" ;
            countSql = countSql+" and school_name like '%"+account.getSchoolName()+"%'";
        }
        if (!StringUtils.isEmpty(account.getTeacher())){
            sql = sql+" and teacher like '%"+account.getTeacher()+"%'";
            countSql = countSql+ " and teacher like '%"+account.getTeacher()+"%'";
        }
        if (!StringUtils.isEmpty(account.getState())){
            sql = sql+" and state="+account.getState();
            countSql = countSql+" and state="+account.getState();
        }
        if (!StringUtils.isEmpty(account.getMobile())){
            sql = sql+" and mobile like '%"+account.getMobile()+"%'";
            countSql = countSql+" and mobile like '%"+account.getMobile()+"%'";
        }

        Query countQuery = entityManager.createNativeQuery(countSql);
        long total = ((BigInteger)countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(sql);
        query.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(AccountListVo.class));
        List list = query.getResultList();

        Page page = new PageImpl(list,pageable,total);
        return page;
    }


    @Override
    public List listArea(Integer id) {
        return areaDao.findByPid(id);
    }

    @Override
    public int userSum() {
        return accountDao.userSum();
    }
}