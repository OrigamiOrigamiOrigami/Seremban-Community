package com.furong.admin.exception;

import com.furong.exception.CustomerException;
import com.furong.exception.ErrorCode;
import com.furong.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理器
 * @author Origami
 * @date 2025/9/10
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(CustomerException.class)
    public Result<String> handleCustomerException(CustomerException e) {
        log.error("业务异常：{}", e.getMessage());
        // 优先使用异常中的错误码和消息，如果没有则使用默认值
        Integer code = e.getCode() != null ? e.getCode() : ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        String message = e.getMessage() != null ? e.getMessage() : ErrorCode.INTERNAL_SERVER_ERROR.getMessage();
        return Result.buildFail(code, message);
    }

    /**
     * 处理参数校验异常（@RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 获取第一个验证错误，避免错误消息过长
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : ErrorCode.VALIDATION_FAILED.getMessage();
        log.error("参数校验失败：{}", errorMessage);
        return Result.buildFail(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
    }

    /**
     * 处理参数绑定异常（@ModelAttribute）
     */
    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException e) {
        // 获取第一个验证错误，避免错误消息过长
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : ErrorCode.VALIDATION_FAILED.getMessage();
        log.error("参数绑定失败：{}", errorMessage);
        return Result.buildFail(ErrorCode.VALIDATION_FAILED.getCode(), errorMessage);
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.buildFail(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
