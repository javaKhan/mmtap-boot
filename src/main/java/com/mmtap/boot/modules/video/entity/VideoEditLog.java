package com.mmtap.boot.modules.video.entity;

import com.mmtap.boot.base.MmtapBootBaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_video_edit_log")
public class VideoEditLog extends MmtapBootBaseEntity {
    private String vid;
    private String state;
}
