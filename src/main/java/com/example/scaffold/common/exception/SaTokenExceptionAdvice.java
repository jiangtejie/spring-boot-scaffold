package com.example.scaffold.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.example.scaffold.common.api.ApiResult;
import com.example.scaffold.common.api.ApiResults;
import com.example.scaffold.common.web.WebResponses;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Sa-Token 认证异常全局处理，统一为 {@link ApiResult} JSON。
 */
@RestControllerAdvice
@Order(0)
public class SaTokenExceptionAdvice {

    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<ApiResult<Void>> handleNotLogin(NotLoginException ex) {
        return WebResponses.status(
                HttpStatus.UNAUTHORIZED, ApiResults.fail(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<ApiResult<Void>> handleNotPermission(NotPermissionException ex) {
        return WebResponses.status(
                HttpStatus.FORBIDDEN, ApiResults.fail(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }
}
