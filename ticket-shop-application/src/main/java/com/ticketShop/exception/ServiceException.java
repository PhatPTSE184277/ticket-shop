package com.ticketShop.exception;

import com.ticketShop.exception.enums.ResultCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final ResultCode resultCode;

    public ServiceException(ResultCode resultCode) {
        super(resultCode.message());
        this.resultCode = resultCode;
    }

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.ERROR;
    }
}
