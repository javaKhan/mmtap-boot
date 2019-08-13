package com.mmtap.boot.modules.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author mmtap
 */
@Data
@AllArgsConstructor
public class RedisVo {

    private String key;

    private String value;
}
