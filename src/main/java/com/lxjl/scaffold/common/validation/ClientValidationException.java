package com.lxjl.scaffold.common.validation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 校验失败异常，由全局异常处理器转为统一 {@code ApiResult}。
 */
public final class ClientValidationException extends RuntimeException {

    private final Map<String, List<String>> fieldErrors;

    public ClientValidationException(Map<String, List<String>> fieldErrors) {
        super("参数校验失败");
        this.fieldErrors = Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
