package com.mmtap.boot.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author mmtap
 */
@Data
public class IpLocate implements Serializable {

    private String retCode;

    private City result;
}

