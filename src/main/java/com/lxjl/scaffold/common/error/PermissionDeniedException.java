package com.lxjl.scaffold.common.error;

import lombok.Getter;

/**
 * 权限不足异常。
 */
@Getter
public class PermissionDeniedException extends AppBizException {

    private final String requiredPermission;
    private final String resourceType;
    private final Object resourceId;

    public PermissionDeniedException(String requiredPermission, String resourceType, Object resourceId) {
        super(AppErrorCode.AUTH_PERMISSION_DENIED,
                String.format("无权操作%s: %s，需要权限: %s", resourceType, resourceId, requiredPermission));
        this.requiredPermission = requiredPermission;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public PermissionDeniedException(String message) {
        super(AppErrorCode.AUTH_PERMISSION_DENIED, message);
        this.requiredPermission = null;
        this.resourceType = null;
        this.resourceId = null;
    }
}
