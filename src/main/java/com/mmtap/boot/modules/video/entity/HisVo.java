package com.mmtap.boot.modules.video.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HisVo {
    private String id;
    private String schoolName;
    private String account;
    private String typeName;
    private String grade;
    private String videoName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date creteTime;
    private String address;
}
