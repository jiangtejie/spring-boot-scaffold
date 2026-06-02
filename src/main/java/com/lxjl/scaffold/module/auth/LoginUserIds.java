package com.lxjl.scaffold.module.auth;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 当前会话登录用户工具。
 *
 * @deprecated 优先注入 {@link CurrentUser} 接口，便于切换认证框架。
 *             静态工具类保留用于非 Spring 管理的工具代码。
 */
@Deprecated
public final class LoginUserIds {

    private LoginUserIds() {}

    public static long userIdLong() {
        return StpUtil.getLoginIdAsLong();
    }
}
