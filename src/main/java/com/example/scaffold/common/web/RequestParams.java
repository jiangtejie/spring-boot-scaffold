package com.example.scaffold.common.web;

import com.example.scaffold.common.error.AppBizException;
import com.example.scaffold.common.error.AppErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * HTTP 查询/路径参数通用解析（与业务异常码对齐）。
 */
public final class RequestParams {

    private RequestParams() {}

    public static long requireQueryLong(HttpServletRequest request, String name, String missingMessage) {
        String raw = request.getParameter(name);
        if (raw == null || raw.isBlank())
            throw new AppBizException(AppErrorCode.PARAM_MISSING, missingMessage);
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "参数 " + name + " 不是合法整数");
        }
    }

    public static LocalDate requireIsoDateQuery(HttpServletRequest request, String name, String missingMessage) {
        String raw = request.getParameter(name);
        if (raw == null || raw.isBlank())
            throw new AppBizException(AppErrorCode.PARAM_MISSING, missingMessage);
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "日期格式不正确，应为 yyyy-MM-dd");
        }
    }

    public static long requirePathLong(String raw, String name) {
        if (raw == null || raw.isEmpty())
            throw new AppBizException(AppErrorCode.PARAM_MISSING, "路径 " + name + " 不能为空");
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            throw new AppBizException(AppErrorCode.PARAM_FORMAT_ERROR, "路径 " + name + " 不合法");
        }
    }
}
