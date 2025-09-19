package com.furong.exception;

/**
 * Customer相关错误码枚举
 * 定义系统中Customer相关的错误码和对应消息
 *
 * @author Origami
 */

public enum ErrorCode {

    // ========== 通用错误码 ==========
    SUCCESS(200, "操作成功"),
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),

    // ========== 数据验证错误码 ==========
    VALIDATION_FAILED(400, "数据验证失败"),
    PARAMETER_MISSING(400, "缺少必要参数"),
    PARAMETER_INVALID(400, "参数格式不正确"),

    // ========== 具体验证错误码 ==========
    ROLE_NAME_REQUIRED(400, "角色名称不能为空"),
    ROLE_NAME_LENGTH_INVALID(400, "角色名称长度必须在2-20个字符之间"),
    PERMISSION_NAME_REQUIRED(400, "权限名称不能为空"),
    PERMISSION_CODE_REQUIRED(400, "权限标识不能为空"),
    PERMISSION_CODE_FORMAT_INVALID(400, "权限标识格式不正确"),

    // ========== 用户相关错误码 ==========
    USER_NOT_FOUND(404, "用户不存在"),
    USER_ALREADY_EXISTS(409, "用户已存在"),
    PHONE_ALREADY_EXISTS(409, "手机号已存在"),
    EMAIL_ALREADY_EXISTS(409, "邮箱已存在"),
    USER_NOT_ALLOW_NULL(400, "用户名不能为空"),
    PASSWORD_NOT_ALLOW_EMPTY(400, "密码不能为空"),
    PHONE_NOT_ALLOW_EMPTY(400, "手机号不能为空"),
    EMAIL_NOT_ALLOW_EMPTY(400, "邮箱不能为空"),
    USER_PASSWORD_ERROR(400, "用户名或密码错误"),
    TOKEN_EXPIRED(401, "token已过期"),
    TOKEN_INVALID(401, "token无效"),
    USER_NOT_LOGIN(401, "用户未登录"),
    USER_NOT_AUTHORIZED(403, "用户未授权"),
    USER_NOT_PERMITTED(405, "用户无权限"),
    USER_NOT_ALLOWED(406, "用户操作不被允许"),

    // ========= 文件相关错误码 ==========
    FILE_UPLOAD_FAILED(500, "文件上传失败"),
    FILE_NOT_EMPTY(400, "文件不能为空"),
    FILE_NAME_NOT_EMPTY(400, "文件名不能为空"),

    // ========= 角色相关错误码 ==========
    ROLE_NOT_EXIST(404, "角色不存在"),
    ROLE_NAME_EXIST(409, "角色名称已存在"),
    ROLE_HAS_ADMIN(409, "角色下还有管理员，无法删除"),

    // ========= 权限相关错误码 ==========
    PERMISSION_NOT_EXIST(404, "权限不存在"),
    PERMISSION_CODE_EXIST(409, "权限标识已存在"),
    PERMISSION_HAS_ROLE(409, "权限下还有角色，无法删除"),
    NO_PERMISSION(403, "没有权限访问"),

    // ========= 系统错误码 ==========
    SYSTEM_ERROR(500, "系统错误");


    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误码获取错误信息
     */
    public static ErrorCode getByCode(Integer code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
    
    /**
     * 判断是否为成功状态
     */
    public boolean isSuccess() {
        return SUCCESS.equals(this);
    }
}
