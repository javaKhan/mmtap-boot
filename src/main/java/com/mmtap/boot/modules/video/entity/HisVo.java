package com.mmtap.boot.modules.video.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Data creteTime;
    private String address;
}
