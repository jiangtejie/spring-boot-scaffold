package com.example.scaffold.module.auth;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;

/**
 * {@link CurrentUser} 的 Sa-Token 实现。
 */
@Component
public class SaTokenCurrentUser implements CurrentUser {

    @Override
    public Long userId() {
        return StpUtil.getLoginIdAsLong();
    }
}
