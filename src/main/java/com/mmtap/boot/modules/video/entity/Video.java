package com.mmtap.boot.modules.video.entity;

import com.mmtap.boot.base.MmtapBootBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mmtap
 */
@Data
@Entity
@Table(name = "t_video")
@ApiModel(value = "视频体系")
public class Video extends MmtapBootBaseEntity {

    private static final long serialVersionUID = 1L;

    private String type_id;
    private String grade;
    private String name;
    private String state;
    private String small;
    private String vod;
    private Integer ordered;
    private String img; //阿里云图片ID

    @Transient
    private String type_name;

    @Transient
    private List pubList = new ArrayList();  //发布历史数据

    @Transient
    private String coverURL;
    @Transient
    private String imgURL;
    @Transient
    private Integer playSum;
}