package com.mmtap.boot.modules.video.serviceimpl;

import com.mmtap.boot.modules.video.dao.VideoLogDao;
import com.mmtap.boot.modules.video.entity.HisPo;
import com.mmtap.boot.modules.video.entity.HisVo;
import com.mmtap.boot.modules.video.entity.VideoLog;
import com.mmtap.boot.modules.video.service.VideoLogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * 视频体系接口实现
 * @author Mmtap
 */
@Slf4j
@Service
@Transactional
public class VideoLogServiceImpl implements VideoLogService {

    @Autowired
    private VideoLogDao videoLogDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveVideoLog(VideoLog videoLog) {
        videoLogDao.save(videoLog);
    }

    /* demo SQL

        select temp.id,temp.schoolName,temp.account,temp.typeName,grade,videoName,creteTime    ,concat(p.name,ci.name,co.name) as address from (
        select l.id,a.province,a.city,a.county,a.school_name as schoolName,a.account,t.`name` as typeName, v.type_id, v.grade,v.`name` as videoName,l.create_time as creteTime from t_video_log l LEFT JOIN t_account a ON l.uid=a.id
        LEFT JOIN t_video v on l.vid=v.id
        left JOIN t_video_type t on v.type_id=t.id
        where province=3 and city=51039 and county = 2985 and v.type_id='176122715269238784' and grade =4 and v.`name` like '%2%' and l.create_time>='2019-09-03 10:00:00' and l.create_time<='2019-09-03 11:00:00'
        order by l.create_time desc
        limit 10,10
        ) temp left join area p on temp.province=p.id
        left join area ci on temp.city = ci.id
        left join area co on temp.county = co.id

         */
    @Override
    public Page getHistory(HisPo param, Pageable pageable) {
        StringBuilder pageSQL = new StringBuilder();
        StringBuilder countSql = new StringBuilder();
        pageSQL.append("select temp.id,temp.schoolName,temp.account,temp.typeName,grade,videoName,creteTime ,concat(p.name,ci.name,co.name) as address from ( select l.id,a.province,a.city,a.county,a.school_name as schoolName,a.account,t.`name` as typeName, v.type_id, v.grade,v.`name` as videoName,l.create_time as creteTime from t_video_log l LEFT JOIN t_account a ON l.uid=a.id LEFT JOIN t_video v on l.vid=v.id left JOIN t_video_type t on v.type_id=t.id  where 1=1 ");
        countSql.append(" SELECT count(*) FROM t_video_log l LEFT JOIN t_account a ON l.uid=a.id LEFT JOIN t_video v ON l.vid=v.id LEFT JOIN t_video_type t ON v.type_id=t.id WHERE 1=1  ");
        if(!ObjectUtils.isEmpty(param.getProvince())){
            pageSQL.append(" AND province="+param.getProvince());
            countSql.append(" AND province="+param.getProvince());
        }
        if(!ObjectUtils.isEmpty(param.getCity())){
            pageSQL.append(" AND city="+param.getCity());
            countSql.append(" AND city="+param.getCity());
        }
        if(!ObjectUtils.isEmpty(param.getCounty())){
            pageSQL.append(" AND county="+param.getCounty());
            countSql.append(" AND county="+param.getCounty());
        }
        if (!StringUtils.isEmpty(param.getSchoolName())){
            pageSQL.append(" and a.school_name like '%"+param.getSchoolName()+"%'");
            countSql.append(" and a.school_name like '%"+param.getSchoolName()+"%'");
        }
        if (!StringUtils.isEmpty(param.getTypeID())){
            pageSQL.append(" AND v.type_id='"+param.getTypeID()+"'");
            countSql.append(" AND v.type_id='"+param.getTypeID()+"'");
        }
        if (!StringUtils.isEmpty(param.getGrade())){
            pageSQL.append(" AND grade="+param.getGrade());
            countSql.append(" AND grade="+param.getGrade());
        }
        if (!StringUtils.isEmpty(param.getCourseName())){
            pageSQL.append(" AND v.`name` LIKE '%"+param.getCourseName()+"%'");
            countSql.append(" AND v.`name` LIKE '%"+param.getCourseName()+"%'");
        }
        if (!StringUtils.isEmpty(param.getStart())){
            pageSQL.append(" AND l.create_time>="+param.getStart());
            countSql.append(" AND l.create_time>="+param.getStart());
        }
        if (!StringUtils.isEmpty(param.getEnd())){
            pageSQL.append(" AND l.create_time<="+param.getEnd());
            countSql.append(" AND l.create_time<="+param.getEnd());
        }

        pageSQL.append(" order by l.create_time desc limit "+ pageable.getPageSize()*pageable.getPageNumber()+"," +pageable.getPageSize())
               .append(" ) temp left join area p on temp.province=p.id left join area ci on temp.city = ci.id left join area co on temp.county = co.id ");

        Query pageQuery = entityManager.createNativeQuery(pageSQL.toString());
        List<HisVo> vos = pageQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(HisVo.class)).getResultList();
        //总数
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        long total = ((BigInteger)countQuery.getSingleResult()).longValue();
        PageImpl<HisVo> page = new PageImpl(vos,pageable,total);
        return page;
    }

}