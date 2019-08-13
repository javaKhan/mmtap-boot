package com.mmtap.boot.modules.video.entity;

import MmtapBootBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Mmtap
 */
@Data
@Entity
@Table(name = "t_video_type")
@TableName("t_video_type")
@ApiModel(value = "视频体系")
public class VideoType extends MmtapBootBaseEntity {

    private static final long serialVersionUID = 1L;

}