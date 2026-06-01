package com.example.scaffold.common.error;

import lombok.Getter;

/**
 * 资源不存在异常。
 */
@Getter
public class ResourceNotFoundException extends AppBizException {

    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(AppErrorCode.RESOURCE_NOT_FOUND,
                String.format("%s不存在: %s", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String resourceName, Object resourceId, String customMessage) {
        super(AppErrorCode.RESOURCE_NOT_FOUND, customMessage);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }
}
