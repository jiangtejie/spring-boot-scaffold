package com.lxjl.scaffold.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * 全局业务异常类。
 *
 * <pre>
 * // 使用默认消息
 * throw new AppBizException(AppErrorCode.RESOURCE_NOT_FOUND);
 *
 * // 自定义消息
 * throw new AppBizException(AppErrorCode.PARAM_INVALID, "昵称长度不能超过128个字符");
 * </pre>
 */
@Getter
public class AppBizException extends RuntimeException {

    private final int bizCode;
    private final HttpStatusCode statusCode;
    private final AppErrorCode errorCode;

    public AppBizException(AppErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), errorCode.getStatusCode());
    }

    public AppBizException(AppErrorCode errorCode, String message) {
        this(errorCode, message, errorCode.getStatusCode());
    }

    public AppBizException(AppErrorCode errorCode, String message, HttpStatusCode statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.bizCode = errorCode.getCode();
        this.statusCode = statusCode;
    }

    public static AppBizException of(AppErrorCode errorCode) {
        return new AppBizException(errorCode);
    }

    public static AppBizException of(AppErrorCode errorCode, String message) {
        return new AppBizException(errorCode, message);
    }
}
