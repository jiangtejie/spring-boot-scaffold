package com.lxjl.scaffold.module.extapi;

import com.lxjl.scaffold.common.api.ApiResult;
import com.lxjl.scaffold.common.api.ApiResults;
import com.lxjl.scaffold.common.error.AppErrorCode;
import com.lxjl.scaffold.config.AppProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 外部 API HMAC 签名验证过滤器，拦截 {@code /api/ext/**}。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExtApiSignFilter extends OncePerRequestFilter {

    private static final String HEADER_TIMESTAMP = "X-Api-Timestamp";
    private static final String HEADER_NONCE = "X-Api-Nonce";
    private static final String HEADER_SIGNATURE = "X-Api-Signature";
    private static final String PATH_PREFIX = "/api/ext/";
    private static final String REDIS_NONCE_PREFIX = "extapi:nonce:";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final AppProperties appProperties;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(PATH_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String timestampHeader = request.getHeader(HEADER_TIMESTAMP);
        String nonce = request.getHeader(HEADER_NONCE);
        String signatureHeader = request.getHeader(HEADER_SIGNATURE);

        if (timestampHeader == null || nonce == null || signatureHeader == null) {
            log.warn("外部 API 缺少签名 Header");
            sendError(response, AppErrorCode.EXT_API_SIGNATURE_MISSING);
            return;
        }

        long timestamp;
        try {
            timestamp = Long.parseLong(timestampHeader);
        } catch (NumberFormatException e) {
            sendError(response, AppErrorCode.EXT_API_SIGNATURE_MISSING);
            return;
        }

        if (!ExtApiSignUtils.isTimestampValid(timestamp)) {
            log.warn("外部 API 时间戳过期: {}", timestamp);
            sendError(response, AppErrorCode.EXT_API_TIMESTAMP_EXPIRED);
            return;
        }

        String redisKey = REDIS_NONCE_PREFIX + nonce;
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(redisKey, "1", java.time.Duration.ofSeconds(ExtApiSignUtils.TIMESTAMP_TOLERANCE_SECONDS));
        if (acquired == null || !acquired) {
            log.warn("外部 API Nonce 重复使用: {}", nonce);
            sendError(response, AppErrorCode.EXT_API_NONCE_REPLAY);
            return;
        }

        String body = readBody(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) uri = uri + "?" + queryString;
        String secret = appProperties.getExtApi().getSecret();
        if (!ExtApiSignUtils.verify(method, uri, body, timestamp, nonce, signatureHeader, secret)) {
            log.warn("外部 API 签名验证失败: {} {}", method, uri);
            sendError(response, AppErrorCode.EXT_API_SIGNATURE_INVALID);
            return;
        }

        chain.doFilter(new BodyRequestWrapper(request, body), response);
    }

    private String readBody(HttpServletRequest request) throws IOException {
        return StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
    }

    private void sendError(HttpServletResponse response, AppErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType("application/json;charset=UTF-8");
        ApiResult<Void> result = ApiResults.fail(errorCode.getCode(), errorCode.getDefaultMessage());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
