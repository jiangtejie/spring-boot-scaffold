package com.lxjl.scaffold.module.auth;

/**
 * 当前登录用户抽象接口。
 *
 * <p>默认实现为 {@link SaTokenCurrentUser}，切换认证框架时只需提供新实现。
 */
public interface CurrentUser {

    Long userId();
}
