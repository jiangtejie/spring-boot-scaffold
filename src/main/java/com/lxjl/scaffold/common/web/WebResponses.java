package com.lxjl.scaffold.common.web;

import com.lxjl.scaffold.common.api.ApiResult;
import com.lxjl.scaffold.common.api.ApiResults;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * ResponseEntity 快捷工厂 — 统一 Content-Type 为 UTF-8 JSON。
 */
public final class WebResponses {

    private static final MediaType JSON_UTF8 =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private WebResponses() {}

    public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
        return ResponseEntity.ok().contentType(JSON_UTF8).body(ApiResults.ok(data));
    }

    public static ResponseEntity<ApiResult<Map<String, Object>>> ok() {
        return ResponseEntity.ok().contentType(JSON_UTF8).body(ApiResults.ok());
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(String msg, T data) {
        return ResponseEntity.ok().contentType(JSON_UTF8).body(ApiResults.ok(msg, data));
    }

    public static <T> ResponseEntity<ApiResult<T>> status(HttpStatus status, ApiResult<T> body) {
        return ResponseEntity.status(status).contentType(JSON_UTF8).body(body);
    }
}
