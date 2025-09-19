package com.furong.exception;

import lombok.Data;

/**
 * 自定义业务异常类
 * 用于处理业务逻辑中的异常情况
 *
 * @author Origami
 */
@Data
public class CustomerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误码枚举
     */
    private ErrorCode errorCode;

    /**
     * 默认构造函数
     */
    public CustomerException() {
        super();
    }

    /**
     * 使用错误消息创建异常
     * @param message 错误消息
     */
    public CustomerException(String message) {
        super(message);
        this.message = message;
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 使用错误码和错误消息创建异常
     * @param code 错误码
     * @param message 错误消息
     */
    public CustomerException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.errorCode = ErrorCode.getByCode(code);
    }

    /**
     * 使用错误码枚举创建异常
     * @param errorCode 错误码枚举
     */
    public CustomerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errorCode = errorCode;
    }
}
