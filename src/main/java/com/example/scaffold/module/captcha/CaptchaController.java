package com.example.scaffold.module.captcha;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.example.scaffold.common.web.WebResponses;
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
    public ResponseModel get(@RequestBody CaptchaVO vo) {
        return captchaService.get(vo);
    }

    @Operation(summary = "校验验证码")
    @PostMapping("/check")
    public ResponseModel check(@RequestBody CaptchaVO vo) {
        return captchaService.check(vo);
    }
}
