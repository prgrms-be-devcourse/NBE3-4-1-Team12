package com.ll.coffeeBean.global.exceptions;

import com.ll.coffeeBean.global.rsData.RsData;
import com.ll.coffeeBean.standard.base.Empty;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ServiceException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public RsData<Empty> getRsData() {
        return new RsData<>(resultCode, msg);
    }
}