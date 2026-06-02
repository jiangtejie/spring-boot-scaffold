package com.lxjl.scaffold.infrastructure.storage;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务抽象接口。
 *
 * <p>默认实现为 {@link LocalFileStorageService}（本地文件系统）。
 * 切换至 OSS / S3 / MinIO 时只需提供新实现。
 */
public interface StorageService {

    String uploadAvatar(Long userId, MultipartFile file);

    String uploadImage(Long userId, String category, MultipartFile file);

    List<String> uploadImages(Long userId, String category, MultipartFile[] files) throws IOException;
}
