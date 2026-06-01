package com.example.scaffold.infrastructure.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 业务时间基准：统一使用东八区。
 */
public final class AppTime {

    public static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    private AppTime() {}

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE);
    }

    public static LocalDate today() {
        return LocalDate.now(ZONE);
    }
}
