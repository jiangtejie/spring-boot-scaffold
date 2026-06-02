package com.lxjl.scaffold.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 全局业务异常错误码枚举（脚手架通用版）。
 *
 * <p>错误码规范：
 * <ul>
 *   <li>2xxx: 成功</li>
 *   <li>4xxx: 客户端错误（参数错误、权限不足等）</li>
 *   <li>5xxx: 服务端错误（业务逻辑错误、第三方服务错误等）</li>
 * </ul>
 *
 * <p>业务模块可扩展此类或新增独立的错误码枚举。
 */
@Getter
public enum AppErrorCode {

    // ==================== 通用错误 (4xxx) ====================
    PARAM_INVALID(40001, "参数无效", HttpStatus.BAD_REQUEST),
    PARAM_MISSING(40002, "缺少必填参数", HttpStatus.BAD_REQUEST),
    PARAM_FORMAT_ERROR(40003, "参数格式错误", HttpStatus.BAD_REQUEST),

    // ==================== 认证授权错误 (4xxx) ====================
    AUTH_TOKEN_INVALID(40101, "Token无效或已过期", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_MISSING(40102, "缺少认证Token", HttpStatus.UNAUTHORIZED),
    AUTH_PERMISSION_DENIED(40301, "权限不足", HttpStatus.FORBIDDEN),
    AUTH_ACCOUNT_LOCKED(40302, "账号已被锁定", HttpStatus.FORBIDDEN),

    // ==================== 资源错误 (4xxx) ====================
    RESOURCE_NOT_FOUND(40401, "资源不存在", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS(40901, "资源已存在", HttpStatus.CONFLICT),
    RESOURCE_IN_USE(40902, "资源正在使用中，无法删除", HttpStatus.CONFLICT),

    // ==================== 请求频率限制 (4xxx) ====================
    API_DUPLICATE_REQUEST(40903, "重复请求，请勿短时间内重复提交", HttpStatus.CONFLICT),
    API_RATE_LIMITED(42901, "请求过于频繁，请稍后再试", HttpStatus.TOO_MANY_REQUESTS),

    // ==================== 外部 API 错误 (508xx) ====================
    EXT_API_SIGNATURE_MISSING(50801, "缺少签名参数", HttpStatus.UNAUTHORIZED),
    EXT_API_SIGNATURE_INVALID(50802, "签名验证失败", HttpStatus.UNAUTHORIZED),
    EXT_API_TIMESTAMP_EXPIRED(50803, "请求时间戳已过期", HttpStatus.UNAUTHORIZED),
    EXT_API_NONCE_REPLAY(50804, "Nonce 重复使用", HttpStatus.UNAUTHORIZED),

    // ==================== 系统错误 (5xxx) ====================
    SYSTEM_ERROR(50000, "系统内部错误", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(50001, "数据库操作失败", HttpStatus.INTERNAL_SERVER_ERROR),
    CACHE_ERROR(50002, "缓存操作失败", HttpStatus.INTERNAL_SERVER_ERROR),
    THIRD_PARTY_SERVICE_ERROR(50200, "第三方服务调用失败", HttpStatus.BAD_GATEWAY);

    private final int code;
    private final String defaultMessage;
    private final HttpStatusCode statusCode;

    AppErrorCode(int code, String defaultMessage) {
        this(code, defaultMessage, HttpStatus.OK);
    }

    AppErrorCode(int code, String defaultMessage, HttpStatusCode statusCode) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.statusCode = statusCode;
    }

    public AppBizException exception() {
        return new AppBizException(this);
    }

    public AppBizException exception(String message) {
        return new AppBizException(this, message);
    }
}
