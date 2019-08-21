package com.mmtap.boot.modules.video.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.dao.VideoDao;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 视频体系接口实现
 * @author Mmtap
 */
@Slf4j
@Service
@Transactional
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;

    @Override
    public VideoDao getRepository() {
        return videoDao;
    }

//    @Override
//    public Page<Video> findByCondition(Video video, SearchVo searchVo, Pageable pageable) {
//
//        return videoDao.findAll(new Specification<Video>() {
//            @Nullable
//            @Override
//            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//
//                // TODO 可添加你的其他搜索过滤条件 默认已有创建时间过滤
//                Path<Date> createTimeField=root.get("createTime");
//
//                List<Predicate> list = new ArrayList<Predicate>();
//
//                //创建时间
//                if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())){
//                    Date start = DateUtil.parse(searchVo.getStartDate());
//                    Date end = DateUtil.parse(searchVo.getEndDate());
//                    list.add(cb.between(createTimeField, start, DateUtil.endOfDay(end)));
//                }
//
//                Predicate[] arr = new Predicate[list.size()];
//                cq.where(list.toArray(arr));
//                return null;
//            }
//        }, pageable);
//    }

    @Override
    public Page listVideo(String grade, String typeID, String state, String word, Pageable pageable) {
        Page page = videoDao.findAll(new Specification<Video>() {
            @Override
            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (!StringUtils.isEmpty(typeID)){
                    list.add(criteriaBuilder.equal(root.get("type_id"),typeID));
                }
                if (!StringUtils.isEmpty(grade)){
                    list.add(criteriaBuilder.equal(root.get("grade"),grade));
                }
                if (!StringUtils.isEmpty(state)){
                    list.add(criteriaBuilder.equal(root.get("state"),state));
                }
                if (!StringUtils.isEmpty(word)){
                    list.add(criteriaBuilder.like(root.get("name"),"%"+word+"%"));
                }


                Predicate[] arr = new Predicate[list.size()];
                query.where(list.toArray(arr));
                return null;
            }
        },pageable);
        return page;
    }

    /**
     * 判断该类型下是否有视频
     * @param tid
     * @return  有视频：true
     *            没有：false
     */
    public boolean isHaveTypeVideo(String tid){
        Video query = new Video();
        query.setType_id(tid);
        query.setId(null);
        Example example = Example.of(query);
        Optional<Video> v = videoDao.findOne(example);
        if (v.isPresent()){
            return true;
        }
        return false;
    }
}