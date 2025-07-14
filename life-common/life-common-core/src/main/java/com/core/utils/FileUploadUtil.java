package com.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 */
@Slf4j
public class FileUploadUtil {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "video/mp4", "video/avi"
    );

    // 上传文件方法
    public static String uploadFile(MultipartFile file, String basePath) {
        try {
            if(basePath == null || basePath.isEmpty()){
                throw new RuntimeException("文件上传路径不能为空");
            }
            // 验证文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("文件名不能为空");
            }

            // 验证文件类型
            String mimeType = file.getContentType();
            if (mimeType == null || !ALLOWED_TYPES.contains(mimeType)) {
                throw new RuntimeException("不允许的文件类型");
            }

            // 验证文件大小
            long maxSize = "image".equals(mimeType.split("/")[0]) ? 50 * 1024 * 1024 : 200 * 1024 * 1024; // 图片最大50MB，视频最大200MB
            if (file.getSize() > maxSize) {
                throw new RuntimeException("文件大小超出限制");
            }
            // 生成安全文件名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + suffix;

            // 安全路径处理
            Path basePathResolved = Paths.get(basePath).toAbsolutePath().normalize();
            Path targetPath = basePathResolved.resolve(fileName).normalize();

            log.info("文件basePathResolved: {}", basePathResolved);
            log.info("文件targetPath: {}", targetPath);
            if (!targetPath.startsWith(basePathResolved)) {
                throw new RuntimeException("非法文件路径");
            }

            // 高效写入文件
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath.toFile());

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("媒体上传失败", e);
        }
    }

    // 下载文件方法
    public static ResponseEntity<Resource> downloadFile(String fileName, String basePath) {
        try {
            // 安全路径处理
            log.info("文件basePath: {}", basePath + ", fileName:" + fileName);

            // 使用绝对路径
            Path basePathResolved = Paths.get(basePath).toAbsolutePath().normalize();
            String normalizedFileName = fileName.replace('\\', File.separatorChar).replace('/', File.separatorChar);
            Path filePath = basePathResolved.resolve(normalizedFileName).normalize();

            log.info("文件basePathResolved: {}", basePathResolved);
            log.info("文件normalizedFileName: {}", normalizedFileName);
            log.info("文件filePath: {}", filePath);

            if (!filePath.startsWith(basePathResolved) || !Files.exists(filePath)) {
                throw new RuntimeException("文件不存在");
            }

            // 获取文件资源
            Resource resource = new UrlResource(filePath.toUri());

            // 自动检测MIME类型
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 中文文件名处理（兼容不同浏览器）
            String encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";

            // 构建响应
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    // 删除文件方法
    public static void deleteFile(String fileName, String basePath) {
        try {
            Path filePath = Paths.get(basePath).resolve(fileName).normalize();
            log.info("文件filePath: {}", filePath);
            log.info("文件basePath: {}", basePath);
            log.info("文件fileName: {}", fileName);
            if (filePath.startsWith(Paths.get(basePath)) && Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("文件不存在");
            }
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }
}
