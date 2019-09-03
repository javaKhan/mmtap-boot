package com.mmtap.boot.modules.video.serviceimpl;

import com.mmtap.boot.modules.video.dao.VideoDao;
import com.mmtap.boot.modules.video.dao.VideoTypeDao;
import com.mmtap.boot.modules.video.entity.Video;
import com.mmtap.boot.modules.video.entity.VideoType;
import com.mmtap.boot.modules.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
    @Autowired
    private VideoTypeDao videoTypeDao;

    @Override
    public VideoDao getRepository() {
        return videoDao;
    }

    @Override
    public Page listVideo(String grade, String typeID, String state, String word, Pageable pageable) {
        Page<Video> page = videoDao.findAll(new Specification<Video>() {
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

        List<VideoType> vl = videoTypeDao.findAll();
        //类型名称
        page.stream().forEach(v->{
            Optional<VideoType> vtc = vl.stream().filter(t->t.getId().equals(v.getType_id())).findFirst();
            if (vtc.isPresent()){
                v.setType_name(vtc.get().getName());
            }
        });

        return page;
    }

    /**
     * 判断该类型下是否有视频
     * @param tid
     * @return  有视频：true
     *            没有：false
     */
    public boolean isHaveTypeVideo(String tid){
        List<Video> temp = videoDao.findByType_id(tid);
        if (temp.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public Optional<Video> findByVid(String vid) {
        return videoDao.findById(vid);
    }

    @Override
    public int videoSum() {
        return videoDao.videoSum();
    }

    @Override
    @Transactional
    public Video saveVideo(Video video) {
        //更新顺序
        if (null!=video.getOrdered()){
            videoDao.updateOrdered(video.getOrdered(),video.getType_id());
        }
        //保存视频
        return videoDao.save(video);
    }

}