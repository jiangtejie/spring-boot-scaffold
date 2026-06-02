package com.lxjl.scaffold.module.upload;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.lxjl.scaffold.common.web.WebResponses;
import com.lxjl.scaffold.infrastructure.storage.StorageService;
import com.lxjl.scaffold.module.auth.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 — 头像与图片。
 */
@Tag(name = "文件上传", description = "头像与图片上传（需登录）")
@RestController
@SaCheckLogin
public class UploadController {

    private final StorageService storageService;
    private final CurrentUser currentUser;

    public UploadController(StorageService storageService, CurrentUser currentUser) {
        this.storageService = storageService;
        this.currentUser = currentUser;
    }

    @Operation(summary = "上传用户头像", description = "仅支持 jpg/jpeg/png/gif，最大 10MB")
    @PostMapping(value = "/api/upload/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> avatar(@Parameter(description = "图片文件", required = true) @RequestPart("file") MultipartFile file) {
        Long userId = currentUser.userId();
        String url = storageService.uploadAvatar(userId, file);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("userId", userId);
        return WebResponses.ok("上传成功", data);
    }

    @Operation(summary = "上传单张图片", description = "category 为业务分类目录名")
    @PostMapping(value = "/api/upload/image/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> image(
            @Parameter(description = "分类目录", example = "common") @PathVariable("category") String category,
            @Parameter(description = "图片文件", required = true) @RequestPart("file") MultipartFile file) {
        Long userId = currentUser.userId();
        String url = storageService.uploadImage(userId, category, file);
        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("userId", userId);
        data.put("category", category);
        return WebResponses.ok("上传成功", data);
    }

    @Operation(summary = "批量上传图片")
    @PostMapping(value = "/api/upload/images/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> images(
            @Parameter(description = "分类目录", example = "common") @PathVariable("category") String category,
            @Parameter(description = "多个图片文件", required = true) @RequestPart("files") MultipartFile[] files) throws IOException {
        Long userId = currentUser.userId();
        List<String> urls = storageService.uploadImages(userId, category, files);
        Map<String, Object> data = new HashMap<>();
        data.put("urls", urls);
        data.put("count", urls.size());
        data.put("userId", userId);
        data.put("category", category);
        return WebResponses.ok("上传成功", data);
    }
}
