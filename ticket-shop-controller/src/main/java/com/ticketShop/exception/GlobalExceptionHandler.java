package com.ticketShop.exception;

import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Bắt các lỗi nghiệp vụ do code chủ động ném ra (ServiceException)
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMessage<Object> handleServiceException(ServiceException e) {
        log.warn("Business Exception: [{}] {}", e.getResultCode().code(), e.getMessage());
        // Sửa thành gọi 1 tham số ResultCode:
        return ResultUtil.error(e.getResultCode());
    }

    /**
     * Bắt tất cả các lỗi hệ thống không lường trước (NullPointer, SQL Exception, v.v.)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage<Object> handleException(Exception e) {
        log.error("System Exception: ", e);
        return ResultUtil.error(ResultCode.ERROR);
    }
}