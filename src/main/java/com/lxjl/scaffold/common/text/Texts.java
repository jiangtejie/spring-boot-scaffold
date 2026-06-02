package com.lxjl.scaffold.common.text;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串规范化工具 — 委托给 Hutool {@link StrUtil}，保留项目特化方法。
 *
 * @deprecated 直接使用 {@link StrUtil} 替代：{@code trimToNull → StrUtil.trimToNull}、
 *             {@code defaultIfBlank → StrUtil.blankToDefault}
 */
@Deprecated
public final class Texts {

    private Texts() {}

    /** @deprecated 请使用 {@link StrUtil#trimToNull(CharSequence)} */
    @Deprecated
    public static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    /** @deprecated 请使用 {@link StrUtil#trimToNull(CharSequence)} */
    @Deprecated
    public static String trimToNull(String s) {
        return StrUtil.trimToNull(s);
    }

    /** @deprecated 请使用 {@link StrUtil#nullToEmpty(CharSequence)} */
    @Deprecated
    public static String defaultToEmpty(String s) {
        return (s == null) ? "" : s.trim();
    }

    /** @deprecated 请使用 {@link StrUtil#blankToDefault(CharSequence, CharSequence)} */
    @Deprecated
    public static String defaultIfBlank(String s, String defaultValue) {
        return StrUtil.blankToDefault(s, defaultValue);
    }

    /**
     * 确保字符串是合法的 JSON 对象，否则返回 {@code "{}"}。
     */
    public static String jsonObjectOrEmpty(String json) {
        if (json == null || json.isBlank()) return "{}";
        String trimmed = json.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ? trimmed : "{}";
    }
}
