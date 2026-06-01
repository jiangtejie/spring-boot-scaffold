package com.example.scaffold.common.exception;

import com.example.scaffold.common.api.ApiResult;
import com.example.scaffold.common.api.ApiResults;
import com.example.scaffold.common.error.AppBizException;
import com.example.scaffold.common.error.PermissionDeniedException;
import com.example.scaffold.common.error.ResourceNotFoundException;
import com.example.scaffold.common.validation.ClientValidationException;
import com.example.scaffold.common.validation.ValidationException;
import com.example.scaffold.common.web.WebResponses;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(basePackages = "com.example.scaffold.module")
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(AppBizException.class)
    public ResponseEntity<ApiResult<Void>> handleAppBiz(AppBizException ex) {
        logSubclassDetails(ex);
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return WebResponses.status(status, ApiResults.fail(ex.getBizCode(), ex.getMessage()));
    }

    private static void logSubclassDetails(AppBizException ex) {
        if (ex instanceof ResourceNotFoundException r) {
            log.warn("业务异常: code={}, resource={}/{}, message={}",
                    r.getBizCode(), r.getResourceName(), r.getResourceId(), r.getMessage());
        } else if (ex instanceof PermissionDeniedException p) {
            String detail = p.getRequiredPermission() != null
                    ? String.format("need=%s, resource=%s/%s", p.getRequiredPermission(), p.getResourceType(), p.getResourceId())
                    : "无明细";
            log.warn("业务异常: code={}, permission={}, message={}", p.getBizCode(), detail, p.getMessage());
        } else if (ex instanceof ValidationException v) {
            String detail = v.getFieldName() != null
                    ? String.format("field=%s, value=%s, rule=%s", v.getFieldName(), v.getFieldValue(), v.getValidationRule())
                    : "无明细";
            log.warn("业务异常: code={}, validation={}, message={}", v.getBizCode(), detail, v.getMessage());
        } else {
            log.warn("业务异常: code={}, message={}", ex.getBizCode(), ex.getMessage());
        }
    }

    @ExceptionHandler(ClientValidationException.class)
    public ResponseEntity<ApiResult<Map<String, List<String>>>> handleClientValidation(ClientValidationException ex) {
        return WebResponses.status(HttpStatus.BAD_REQUEST, ApiResults.validationFail(ex.getFieldErrors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Map<String, List<String>>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        return WebResponses.status(HttpStatus.BAD_REQUEST, ApiResults.validationFail(toFieldErrors(ex.getBindingResult())));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResult<Map<String, List<String>>>> handleBindException(BindException ex) {
        return WebResponses.status(HttpStatus.BAD_REQUEST, ApiResults.validationFail(toFieldErrors(ex.getBindingResult())));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResult<Void>> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String msg = ex.getReason() != null ? ex.getReason() : status.toString();
        return WebResponses.status(status, ApiResults.fail(status.value(), msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGeneric(Exception ex) {
        log.error("API 未处理异常: {}", ex.getMessage(), ex);
        return WebResponses.status(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ApiResults.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务异常"));
    }

    private static Map<String, List<String>> toFieldErrors(BindingResult bindingResult) {
        Map<String, List<String>> map = new HashMap<>();
        for (FieldError fe : bindingResult.getFieldErrors()) {
            map.computeIfAbsent(fe.getField(), k -> new ArrayList<>()).add(fe.getDefaultMessage());
        }
        return map;
    }
}
