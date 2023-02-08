package com.edukg.open.base;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5317403756736254689L;

    private Integer code;

    private String errorMsg;

    public BusinessException(Integer messageCode, String errorMsg) {
        this.code = messageCode;
        this.errorMsg = errorMsg;
    }


}

