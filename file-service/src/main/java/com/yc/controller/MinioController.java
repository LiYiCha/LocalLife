package com.yc.controller;

import com.core.utils.Result;
import com.yc.service.MinioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件名: MinioController
 * 创建者: @一茶
 * 创建时间:2025/5/8 9:49
 * 描述：
 */
@RequestMapping("/api/file")
public class MinioController {
    private final MinioService minioService;
    public MinioController(MinioService minioService){
        this.minioService = minioService;
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = minioService.uploadFile(file);
        return Result.success(fileUrl);
    }

    /**
     * 文件下载
     * @param fileName
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        byte[] content = minioService.downloadFileAsBytes(fileName);

        if (content.length == 0) {
            throw new IllegalArgumentException("文件不存在");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }


}
