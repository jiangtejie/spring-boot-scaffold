package com.lxjl.scaffold.common.query;

import com.mybatisflex.core.query.QueryWrapper;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通用查询构建器 — MyBatis-Flex QueryWrapper 便捷封装。
 *
 * <p>MyBatis-Flex {@link com.mybatisflex.core.query.QueryWrapper} 已内置 eq/ne/gt/ge/lt/le/in/like
 * 等类型安全方法，本类中同名方法已废弃，推荐直接使用 QueryWrapper 原生 API。
 *
 * <p>本类保留 {@code betweenTime} 便捷方法和 {@code orderByAsc/Desc} 别名。
 */
public final class QueryBuilder {

    private QueryBuilder() {}

    public static QueryWrapper create() {
        return QueryWrapper.create();
    }

    @Deprecated
    public static QueryWrapper eq(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " = ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper ne(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " != ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper gt(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " > ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper ge(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " >= ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper lt(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " < ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper le(QueryWrapper w, String column, Object value) {
        return value != null ? w.where(column + " <= ?", value) : w;
    }

    @Deprecated
    public static QueryWrapper in(QueryWrapper w, String column, List<?> values) {
        return (values != null && !values.isEmpty()) ? w.where(column + " IN (?)", values) : w;
    }

    @Deprecated
    public static QueryWrapper like(QueryWrapper w, String column, String value) {
        return (value != null && !value.isEmpty()) ? w.where(column + " LIKE ?", "%" + value + "%") : w;
    }

    public static QueryWrapper betweenTime(QueryWrapper w, String column, LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) return w.where(column + " BETWEEN ? AND ?", start, end);
        if (start != null) return w.where(column + " >= ?", start);
        if (end != null) return w.where(column + " <= ?", end);
        return w;
    }

    public static QueryWrapper orderByDesc(QueryWrapper w, String column) {
        return w.orderBy(column, false);
    }

    public static QueryWrapper orderByAsc(QueryWrapper w, String column) {
        return w.orderBy(column, true);
    }
}
