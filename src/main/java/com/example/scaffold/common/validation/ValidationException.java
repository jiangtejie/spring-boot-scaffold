package com.example.scaffold.common.validation;

import com.example.scaffold.common.error.AppBizException;
import com.example.scaffold.common.error.AppErrorCode;
import lombok.Getter;

/**
 * 参数验证异常。
 */
@Getter
public class ValidationException extends AppBizException {

    private final String fieldName;
    private final Object fieldValue;
    private final String validationRule;

    public ValidationException(String fieldName, Object fieldValue, String validationRule, String message) {
        super(AppErrorCode.PARAM_INVALID, message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.validationRule = validationRule;
    }

    public ValidationException(String message) {
        super(AppErrorCode.PARAM_INVALID, message);
        this.fieldName = null;
        this.fieldValue = null;
        this.validationRule = null;
    }
}
