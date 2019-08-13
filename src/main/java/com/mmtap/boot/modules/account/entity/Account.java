package com.mmtap.boot.modules.account.entity;

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
@Table(name = "t_account")
@TableName("t_account")
@ApiModel(value = "用户体系")
public class Account extends MmtapBootBaseEntity {

    private static final long serialVersionUID = 1L;

}