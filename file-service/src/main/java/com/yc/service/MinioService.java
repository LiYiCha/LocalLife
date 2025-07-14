package com.yc.service;

import com.yc.config.FileTypeValidator;
import com.yc.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件名: MinioService
 * 创建者: @一茶
 * 创建时间:2025/5/8 8:59
 * 描述：
 */
public class MinioService {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 文件上传
     * @param file
     * @return
     */
    public String uploadFile(MultipartFile file) {
        try {
            // 检查文件名
            if (file.getOriginalFilename() == null) {
                throw new IllegalArgumentException("文件名不能为空");
            }

            // 检查文件大小（10MB）
            if (file.getSize() > 1024 * 1024 * 10) {
                throw new IllegalArgumentException("文件大小不能超过10MB");
            }

            // 检查文件类型（通过魔数校验）
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            if (!"jpg".equals(suffix) && !"png".equals(suffix)) {
                throw new IllegalArgumentException("仅支持上传jpg或png格式的图片");
            }

            if (!FileTypeValidator.isValidFileType(file.getInputStream(), suffix)) {
                throw new IllegalArgumentException("文件类型不匹配，请上传合法的图片文件");
            }

            // 检查存储桶是否存在，如果不存在则创建
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 生成文件名
            String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8) + "." + suffix;

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            // 返回文件访问路径
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(7 * 24 * 60 * 60) // 有效期7天
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 文件下载
     * @param fileName
     * @return
     */
    public InputStream downloadFile(String fileName) {
        try {
            //检查文件名是否为空
            if (fileName == null) {
                return null;
            }
            //检查文件是否存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                return null;
            }
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
    /**
     * 下载文件并返回字节数组（适用于文件较小的情况）
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public byte[] downloadFileAsBytes(String fileName) {
        try (InputStream inputStream = downloadFile(fileName)) {
            if (inputStream == null) {
                return new byte[0]; // 或抛出异常
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

}
