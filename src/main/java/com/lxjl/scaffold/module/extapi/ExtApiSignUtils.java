package com.lxjl.scaffold.module.extapi;

import cn.hutool.crypto.SecureUtil;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;

/**
 * 外部 API HMAC-SHA256 签名工具（基于 Hutool {@link SecureUtil}）。
 *
 * <p>请求端签名：payload = {@code method + "\n" + uri + "\n" + body + "\n" + timestamp + "\n" + nonce}，结果 hex。
 * <p>加入 method + uri 防止签名被重放到不同接口。
 */
@Slf4j
public final class ExtApiSignUtils {

    static final long TIMESTAMP_TOLERANCE_SECONDS = 300;

    private ExtApiSignUtils() {}

    public static String sign(String method, String uri, String body, long timestamp, String nonce, String secret) {
        String payload = method + "\n" + uri + "\n" + body + "\n" + timestamp + "\n" + nonce;
        return SecureUtil.hmacSha256(secret).digestHex(payload);
    }

    public static boolean verify(String method, String uri, String body, long timestamp,
                                  String nonce, String signature, String secret) {
        if (secret == null || secret.isBlank()) {
            log.error("外部 API 签名密钥未配置");
            return false;
        }
        return sign(method, uri, body, timestamp, nonce, secret).equalsIgnoreCase(signature);
    }

    public static boolean isTimestampValid(long timestampSeconds) {
        return Math.abs(Instant.now().getEpochSecond() - timestampSeconds) <= TIMESTAMP_TOLERANCE_SECONDS;
    }
}
