package com.example.scaffold.module.extapi;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * 外部 API HMAC-SHA256 签名工具。
 *
 * <p>请求端签名：payload = {@code method + "\n" + uri + "\n" + body + "\n" + timestamp + "\n" + nonce}，结果 hex。
 * <p>加入 method + uri 防止签名被重放到不同接口。
 */
@Slf4j
public final class ExtApiSignUtils {

    static final long TIMESTAMP_TOLERANCE_SECONDS = 300;
    static final String HMAC_ALGORITHM = "HmacSHA256";

    private ExtApiSignUtils() {}

    public static String sign(String method, String uri, String body, long timestamp, String nonce, String secret) {
        String payload = method + "\n" + uri + "\n" + body + "\n" + timestamp + "\n" + nonce;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 签名计算失败", e);
        }
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
