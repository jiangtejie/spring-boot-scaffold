package com.example.scaffold.module.auth;

import cn.dev33.satoken.stp.StpUtil;
import com.example.scaffold.common.web.WebResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth 第三方登录。
 *
 * <p>支持 GitHub / Gitee / 微信开放平台 等，配置见 application.yml。
 */
@Tag(name = "第三方登录", description = "OAuth 登录回调")
@RestController
@RequestMapping("/api/auth/oauth")
public class AuthController {

    private final Map<String, AuthRequest> authRequestMap;

    public AuthController(Map<String, AuthRequest> authRequestMap) {
        this.authRequestMap = authRequestMap;
    }

    @Operation(summary = "获取授权 URL")
    @GetMapping("/{source}")
    public String authorize(@PathVariable String source) {
        AuthRequest request = authRequestMap.get(source.toUpperCase());
        if (request == null) throw new IllegalArgumentException("不支持的登录来源: " + source);
        return request.authorize(AuthStateUtils.createState());
    }

    @Operation(summary = "OAuth 回调")
    @GetMapping("/{source}/callback")
    public Object callback(@PathVariable String source, AuthCallback callback) {
        AuthRequest request = authRequestMap.get(source.toUpperCase());
        if (request == null) throw new IllegalArgumentException("不支持的登录来源: " + source);
        var response = request.login(callback);
        // 业务自行处理用户信息 → 创建/绑定本地账号 → 登录
        AuthUser user = (AuthUser) response.getData();
        StpUtil.login(user.getUuid());
        return WebResponses.ok(Map.of(
                "nickname", user.getNickname(),
                "avatar", user.getAvatar(),
                "source", user.getSource()
        ));
    }
}
