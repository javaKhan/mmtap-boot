package com.mmtap.boot.modules.video.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mmtap.boot.common.vo.SearchVo;
import com.mmtap.boot.modules.video.dao.VideoTypeDao;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.VideoTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class VideoTypeServiceImpl implements VideoTypeService {

    @Autowired
    private VideoTypeDao videoTypeDao;

    @Override
    public VideoTypeDao getRepository() {
        return videoTypeDao;
    }

//    @Override
//    public Page<VideoType> findByCondition(VideoType videoType, SearchVo searchVo, Pageable pageable) {
//
//        return videoTypeDao.findAll(new Specification<VideoType>() {
//            @Nullable
//            @Override
//            public Predicate toPredicate(Root<VideoType> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
    public List<VideoType> allType() {
        return videoTypeDao.findAll();
    }

    @Override
    public Optional<VideoType> findByID(String id) {
        return videoTypeDao.findById(id);
    }

    @Override
    public List countTypeVideo() {
        return videoTypeDao.countTypeVideo();
    }

    @Override
    @Transactional
    public void saveVideoType(VideoType vt) {
        //更新顺序
//        if (null!=vt.getOrdered()){
//            videoTypeDao.updateOrder(vt.getOrdered());
//        }
        //保存新的
        videoTypeDao.save(vt);
    }

    @Override
    public List<VideoType> findByVideoName(String name) {
        return videoTypeDao.findByName(name);
    }

    @Override
    public List findUserVideoType(List<String> tids) {
        return videoTypeDao.findByIdIn(tids);
    }
}