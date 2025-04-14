package edu.upc.lll.common.exception;

public enum ErrorCodeEnum {
    SUCCESS(200, "成功"),
    VALIDATION_ERROR(400, "参数校验错误"),
    UNAUTHORIZED(401, "请重新登录"),
    FORBIDDEN(403, "没有权限"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器内部错误"),

    // 业务自定义错误码范围：1000-1999
    LOGIN_FAILED(1001, "用户名或密码错误"),
    TOKEN_EXPIRED(1002, "登录状态已过期");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
