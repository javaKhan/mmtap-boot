package com.mmtap.boot.modules.video.entity;

import com.mmtap.boot.base.MmtapBootBaseEntity;
import com.mmtap.boot.common.utils.ObjectUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Mmtap
 */
@Data
@Entity
@Table(name = "t_video_type")
@ApiModel(value = "视频体系")
public class VideoType extends MmtapBootBaseEntity implements Comparable<VideoType> {

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private Integer ordered;
    private String icon;
    @Transient
    private String amount;

    @Override
    public int compareTo(VideoType o) {
        if (ObjectUtils.isEmpty(this.getOrdered())){
            return 1;
        }
        if (ObjectUtils.isEmpty(o.getOrdered())){
            return -1;
        }
        return this.getOrdered()-o.getOrdered();
    }
}