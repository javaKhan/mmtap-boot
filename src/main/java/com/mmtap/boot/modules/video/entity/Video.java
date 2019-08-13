package com.mmtap.boot.modules.video.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@Table(name = "t_video")
@TableName("t_video")
@ApiModel(value = "视频体系")
public class Video extends MmtapBootBaseEntity {

    private static final long serialVersionUID = 1L;

}