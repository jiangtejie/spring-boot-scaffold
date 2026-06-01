package com.example.scaffold.common.text;

/**
 * 字符串规范化工具 — 表单 / JSON 补丁等场景复用。
 */
public final class Texts {

    private Texts() {}

    public static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    public static String trimToNull(String s) {
        if (s == null) return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String defaultToEmpty(String s) {
        return (s == null) ? "" : s.trim();
    }

    public static String defaultIfBlank(String s, String defaultValue) {
        return (s == null || s.isBlank()) ? defaultValue : s;
    }

    public static String jsonObjectOrEmpty(String json) {
        if (json == null || json.isBlank()) return "{}";
        String trimmed = json.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ? trimmed : "{}";
    }
}
