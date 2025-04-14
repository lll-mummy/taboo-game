export const STATUS_CODES = {
    // 成功相关状态码
    SUCCESS: { code: 200, message: '成功' },

    // 错误相关状态码
    VALIDATION_ERROR: { code: 400, message: '参数校验错误' },
    UNAUTHORIZED: { code: 401, message: '请重新登录' },
    FORBIDDEN: { code: 403, message: '没有权限' },
    NOT_FOUND: { code: 404, message: '资源不存在' },
    SERVER_ERROR: { code: 500, message: '服务器内部错误' },

    // 业务自定义错误码
    LOGIN_FAILED: { code: 1001, message: '用户名或密码错误' },
    TOKEN_EXPIRED: { code: 1002, message: '登录状态已过期' },

    // 默认错误消息
    UNKNOWN_ERROR: { code: 9999, message: '未知错误，请稍后重试' },
};
