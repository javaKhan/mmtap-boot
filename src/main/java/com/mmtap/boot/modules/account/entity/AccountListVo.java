package com.mmtap.boot.modules.account.entity;

import com.mmtap.boot.modules.video.entity.VideoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListVo {
    private String id;
    private String schoolid;
    private String school_name;
    private String local;
    private String teacher;
    private String mobile;
    private Integer state;
    private String perms;
    private List<VideoType> vts = new ArrayList();
}
