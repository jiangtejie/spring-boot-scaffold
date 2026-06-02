package com.lxjl.scaffold.common.validation;

import com.lxjl.scaffold.common.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 业务数据验证器 — 常用字段校验方法。
 */
public final class BusinessValidator {

    private BusinessValidator() {}

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty())
            throw new ValidationException(fieldName, value, "非空", fieldName + "不能为空");
    }

    public static void validateLength(String value, int min, int max, String fieldName) {
        if (value != null) {
            int len = value.length();
            if (len < min || len > max)
                throw new ValidationException(fieldName, value,
                        String.format("长度%d-%d", min, max),
                        String.format("%s长度必须在%d-%d之间，当前: %d", fieldName, min, max, len));
        }
    }

    public static void validateRange(Number value, Number min, Number max, String fieldName) {
        if (value != null) {
            double dv = value.doubleValue();
            double minV = min != null ? min.doubleValue() : Double.MIN_VALUE;
            double maxV = max != null ? max.doubleValue() : Double.MAX_VALUE;
            if (dv < minV || dv > maxV)
                throw new ValidationException(fieldName, value,
                        String.format("范围%.2f-%.2f", minV, maxV),
                        String.format("%s必须在%.2f-%.2f之间，当前: %.2f", fieldName, minV, maxV, dv));
        }
    }

    public static void validateDateRange(LocalDate date, LocalDate min, LocalDate max, String fieldName) {
        if (date != null) {
            if (min != null && date.isBefore(min))
                throw new ValidationException(fieldName, date, "不早于" + min, fieldName + "不能早于" + min);
            if (max != null && date.isAfter(max))
                throw new ValidationException(fieldName, date, "不晚于" + max, fieldName + "不能晚于" + max);
        }
    }

    public static void validateDateTimeRange(LocalDateTime dt, LocalDateTime min, LocalDateTime max, String fieldName) {
        if (dt != null) {
            if (min != null && dt.isBefore(min))
                throw new ValidationException(fieldName, dt, "不早于" + min, fieldName + "不能早于" + min);
            if (max != null && dt.isAfter(max))
                throw new ValidationException(fieldName, dt, "不晚于" + max, fieldName + "不能晚于" + max);
        }
    }

    public static void validatePhone(String phone) {
        if (phone != null && !phone.matches("^1[3-9]\\d{9}$"))
            throw new ValidationException("手机号", phone, "格式: 1[3-9]\\d{9}", "手机号格式不正确");
    }

    public static void validateEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new ValidationException("邮箱", email, "标准邮箱格式", "邮箱格式不正确");
    }

    public static void validateTimeOrder(LocalDateTime start, LocalDateTime end, String startName, String endName) {
        if (start != null && end != null && start.isAfter(end))
            throw new ValidationException(startName, start, "不晚于" + endName, startName + "不能晚于" + endName);
    }

    public static void validateDecimalPrecision(BigDecimal value, int maxScale, String fieldName) {
        if (value != null && value.scale() > maxScale)
            throw new ValidationException(fieldName, value,
                    String.format("最多%d位小数", maxScale),
                    String.format("%s最多支持%d位小数", fieldName, maxScale));
    }
}
