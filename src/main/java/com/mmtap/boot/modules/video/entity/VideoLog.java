package com.mmtap.boot.modules.video.entity;

import com.mmtap.boot.base.MmtapBootBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Mmtap
 */
@Data
@Entity
@Table(name = "t_video_log")
@ApiModel(value = "视频播放日志")
public class VideoLog extends MmtapBootBaseEntity {
    private String vid;
    private String uid;
    private String vod;
//    private String sn;      //学校名称
//    private String vn;      //视频名称
}
