package com.lxjl.scaffold.infrastructure.storage;

import com.lxjl.scaffold.common.error.AppBizException;
import com.lxjl.scaffold.common.error.AppErrorCode;
import com.lxjl.scaffold.config.AppProperties;
import com.lxjl.scaffold.infrastructure.time.AppTime;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储服务 — {@link StorageService} 的本地文件系统实现。
 *
 * <p>目录结构：{@code {uploadDir}/{category}/user_{userId}/{yyyy/MM/dd}/{uuid}.ext}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements StorageService {

    private static final long MAX_IMAGE_BYTES = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif");

    private final AppProperties appProperties;

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        validateImageFile(file);
        return saveFileWithUser(userId, "avatars", file);
    }

    @Override
    public String uploadImage(Long userId, String category, MultipartFile file) {
        validateImageFile(file);
        return saveFileWithUser(userId, category, file);
    }

    @Override
    public List<String> uploadImages(Long userId, String category, MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile f : files) {
            if (!f.isEmpty()) urls.add(uploadImage(userId, category, f));
        }
        return urls;
    }

    private void validateImageFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty())
            throw new AppBizException(AppErrorCode.PARAM_INVALID, "文件名不能为空");
        String lower = filename.toLowerCase();
        if (ALLOWED_EXTENSIONS.stream().noneMatch(lower::endsWith))
            throw new AppBizException(AppErrorCode.PARAM_INVALID, "只支持 JPG、PNG、GIF 格式的图片");
        if (file.getSize() > MAX_IMAGE_BYTES)
            throw new AppBizException(AppErrorCode.PARAM_INVALID, "文件大小不能超过 10MB");
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\"))
            throw new AppBizException(AppErrorCode.PARAM_INVALID, "非法的文件名");
    }

    private String saveFileWithUser(Long userId, String category, MultipartFile file) {
        String uploadDir = appProperties.getUpload().getDir();
        String datePath = AppTime.today().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path dir = Paths.get(uploadDir, category, "user_" + userId, datePath);
        try {
            Files.createDirectories(dir);
            String ext = getExtension(file.getOriginalFilename());
            String uniqueName = UUID.randomUUID() + ext;
            Path filePath = dir.resolve(uniqueName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("文件保存成功: {}", filePath);
            return toPublicUrl(filePath);
        } catch (IOException e) {
            log.error("保存文件失败", e);
            throw new AppBizException(AppErrorCode.SYSTEM_ERROR, "文件保存失败");
        }
    }

    private String toPublicUrl(Path absolute) {
        String pathStr = absolute.toString().replace("\\", "/");
        String baseUrl = appProperties.getUpload().getBaseUrl();
        int idx = pathStr.indexOf("/uploads");
        if (idx >= 0) return baseUrl.replace("/uploads", "") + pathStr.substring(idx);
        // 兼容老路径的 fallback
        String fileName = absolute.getFileName().toString();
        return baseUrl + "/" + fileName;
    }

    private static String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(dot) : "";
    }
}
