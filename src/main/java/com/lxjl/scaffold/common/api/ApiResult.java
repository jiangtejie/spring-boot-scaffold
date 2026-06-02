package com.lxjl.scaffold.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 前后端统一 JSON 形态：{@code { "code", "msg", "data" }}。
 * 成功时 {@code code == 200}。
 */
@JsonInclude(Include.NON_NULL)
@Schema(description = "统一 API 响应：code 为 HTTP 语义或业务状态码；msg 为提示；data 为载荷（可为 null）")
public record ApiResult<T>(
        @Schema(description = "状态码：成功一般为 200", example = "200") int code,
        @Schema(description = "提示信息", example = "ok") String msg,
        @Schema(description = "业务数据，类型随接口变化", implementation = Object.class) T data) {}
