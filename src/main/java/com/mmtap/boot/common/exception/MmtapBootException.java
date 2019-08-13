package com.mmtap.boot.common.exception;

import lombok.Data;

/**
 * @author mmtap
 */
@Data
public class MmtapBootException extends RuntimeException {

    private String msg;

    public MmtapBootException(String msg){
        super(msg);
        this.msg = msg;
    }
}
