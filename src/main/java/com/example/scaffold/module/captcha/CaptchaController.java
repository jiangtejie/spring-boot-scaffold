package com.example.scaffold.module.captcha;

import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.example.scaffold.common.api.ApiResult;
import com.example.scaffold.common.api.ApiResults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行为验证码 — 滑块 / 点选。
 */
@Tag(name = "验证码", description = "滑块/点选行为验证")
@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @Operation(summary = "获取验证码")
    @PostMapping("/get")
    public ApiResult<?> get(@RequestBody CaptchaVO vo) {
        return ApiResults.ok(captchaService.get(vo));
    }

    @Operation(summary = "校验验证码")
    @PostMapping("/check")
    public ApiResult<?> check(@RequestBody CaptchaVO vo) {
        return ApiResults.ok(captchaService.check(vo));
    }
}
