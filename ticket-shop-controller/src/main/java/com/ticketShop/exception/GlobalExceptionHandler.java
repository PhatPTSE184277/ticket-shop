package com.ticketShop.exception;

import com.ticketShop.exception.enums.ResultCode;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Bắt các lỗi nghiệp vụ do Application/Service layer chủ động ném ra
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMessage<Object> handleServiceException(ServiceException e) {
        log.warn("Business Exception: [{}] {}", e.getResultCode().code(), e.getMessage());
        return ResultUtil.error(e.getResultCode());
    }

    /**
     * Bắt lỗi Validation thủ công (như IllegalArgumentException từ TicketEventValidator)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMessage<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Domain/Validation Exception: {}", e.getMessage());
        // Trả về mã PARAMS_ERROR (4002) kèm câu thông báo chi tiết từ exception
        return ResultUtil.error(ResultCode.PARAMS_ERROR.code(), e.getMessage());
    }

    /**
     * Bắt lỗi Validation tự động từ Spring Boot (@Valid trên RequestBody Controller)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMessage<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "Tham số không hợp lệ";
        log.warn("Request Validation Exception: {}", message);
        return ResultUtil.error(ResultCode.PARAMS_ERROR.code(), message);
    }

    /**
     * Bắt tất cả các lỗi hệ thống không lường trước (NullPointerException, SQL Exception, v.v.)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultMessage<Object> handleException(Exception e) {
        log.error("System Exception: ", e);
        return ResultUtil.error(ResultCode.ERROR);
    }
}