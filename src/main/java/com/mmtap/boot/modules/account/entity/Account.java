package com.mmtap.boot.modules.account.entity;

import com.mmtap.boot.base.MmtapBootBaseEntity;
import com.mmtap.boot.modules.video.entity.VideoType;
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
@Table(name = "t_account")
@ApiModel(value = "用户体系")
public class Account extends MmtapBootBaseEntity {

    private static final long serialVersionUID = 1L;
    private String account;
    private String pwd;

    private int province;
    private int city;
    private int county;

    private String schoolID;
    private String schoolName;
    private String teacher;
    private String mobile;
    private int state;
    private String perms;
    private String ipStart;
    private String ipEnd;
    private int role;

    @Transient
    private List<VideoType> vts = new ArrayList();

}