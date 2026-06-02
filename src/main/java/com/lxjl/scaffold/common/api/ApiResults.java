package com.lxjl.scaffold.common.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public final class ApiResults {

    public static final int CODE_OK = 200;
    public static final int CODE_VALIDATION = 400;

    private ApiResults() {}

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(CODE_OK, "ok", data);
    }

    public static <T> ApiResult<T> ok(String msg, T data) {
        return new ApiResult<>(CODE_OK, msg, data);
    }

    public static <T> ApiResult<T> ok() {
        @SuppressWarnings("unchecked")
        T empty = (T) Collections.emptyMap();
        return new ApiResult<>(CODE_OK, "ok", empty);
    }

    public static <T> ApiResult<T> fail(int code, String msg) {
        return new ApiResult<>(code, msg, null);
    }

    public static <T> ApiResult<T> fail(HttpStatusCode status, String msg) {
        return new ApiResult<>(status.value(), msg, null);
    }

    public static <T> ApiResult<T> fromException(ResponseStatusException ex) {
        return fail(ex.getStatusCode(), ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString());
    }

    public static ApiResult<Map<String, List<String>>> validationFail(Map<String, List<String>> fieldErrors) {
        return new ApiResult<>(CODE_VALIDATION, "参数校验失败", fieldErrors);
    }
}
