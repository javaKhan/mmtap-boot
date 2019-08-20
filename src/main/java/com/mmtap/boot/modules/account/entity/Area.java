package com.mmtap.boot.modules.account.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "area")
public class Area {
    @Id
    private int id;
    private int pid;
    private String name;
    private String nail;

    @Transient
    private List child = new ArrayList();
}
