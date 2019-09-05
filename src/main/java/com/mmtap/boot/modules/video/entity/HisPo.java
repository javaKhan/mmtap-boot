package com.mmtap.boot.modules.video.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class HisPo {
    private Integer province ;
    private Integer city;
    private Integer county;
    private String schoolName;
    private String typeID;
    private String grade;
    private String courseName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

}
