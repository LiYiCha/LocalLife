package com.auth.controller;

import com.core.utils.RedisUtil;
import com.core.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 验证码控制器和校验控制器
 * <p/>
 */
@Slf4j
@RestController
@RequestMapping("/api/auth/captcha")
public class CaptchaController {

    @Autowired
    private RedisUtil redis;

    // Redis 键前缀请求次数
    private static final String REQUEST_COUNT_PREFIX = "captcha:requestCount:";
    // Redis 键前缀ip限制
    private static final String IP_LIMIT_PREFIX = "captcha:ipLimit:";
    // 最大请求次数和限制时长
    private static final int MAX_REQUESTS_PER_MINUTE = 20;
    // IP 限制的时长（分钟）
    private static final int IP_LIMIT_DURATION_MINUTES = 5;
    // 验证码过期时长（分钟）
    private static final int CAPTCHA_EXPIRATION_MINUTES = 5;

    /**
     * 生成验证码
     * 每分钟 最多请求次数10
     * @param key
     * @return
     */
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateCaptcha(@RequestParam("key") String key, HttpServletRequest request) {
        try {
            String ipAddress = request.getRemoteAddr();
            String requestCountKey = REQUEST_COUNT_PREFIX + ipAddress;
            String ipLimitKey = IP_LIMIT_PREFIX + ipAddress;

            // 检查 IP 是否被限制
            if (isIpLimited(ipLimitKey)) {
                return ResponseEntity.status(429).body("请求过于频繁，请稍后再试".getBytes());
            }

            // 获取当前请求次数并检查是否超过限制
            Long requestCount = getRequestCount(requestCountKey);
            if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
                limitIp(ipLimitKey);
                return ResponseEntity.status(429).body("请求过于频繁，请稍后再试".getBytes());
            }

            // 增加请求次数
            incrementRequestCount(requestCountKey);

            // 生成随机验证码并存储到 Redis
            String code = generateRandomCode(4);
            storeCaptchaInRedis(key, code);

            // 生成验证码图片并返回
            BufferedImage image = generateImage(code);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(convertImageToBytes(image));
        } catch (Exception e) {
            e.printStackTrace(); // 记录日志
            return ResponseEntity.status(500).body("服务器内部错误".getBytes());
        }
    }

    /**
     * 验证验证码
     */
    @PostMapping("/verify")
    public Result verifyCaptcha(@RequestParam("key") String key, @RequestParam("code") String code) {
        String redisCaptcha = (String) redis.get("captcha:" + key);
        if (redisCaptcha == null || redisCaptcha.isEmpty()) {
            return Result.error("验证码已过期或无效");
        }

        // 校验验证码（忽略大小写）
        if (redisCaptcha.equalsIgnoreCase(code.trim())) {
            return Result.success("验证成功");
        }
        return Result.error("无效验证码");
    }

    // ======================== 辅助方法 ========================

    /**
     * 检查 IP 是否被限制
     */
    private boolean isIpLimited(String ipLimitKey) {
        return redis.hasKey(ipLimitKey);
    }

    /**
     * 获取当前 IP 的请求次数
     */
    private Long getRequestCount(String requestCountKey) {
        Object requestCountObj = redis.get(requestCountKey);
        if (requestCountObj instanceof String) {
            try {
                return Long.parseLong((String) requestCountObj);
            } catch (NumberFormatException e) {
                return 0L; // 如果解析失败，返回默认值 0
            }
        } else if (requestCountObj instanceof Number) {
            return ((Number) requestCountObj).longValue();
        }
        return 0L; // 默认值
    }

    /**
     * 限制 IP
     */
    private void limitIp(String ipLimitKey) {
        redis.set(ipLimitKey, "true", IP_LIMIT_DURATION_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 增加请求次数
     */
    private void incrementRequestCount(String requestCountKey) {
        try {
            // 原子操作初始化计数器（字符串类型）
            Boolean isNew = redis.setIfAbsent(requestCountKey, "0", 1, TimeUnit.MINUTES);
            if (Boolean.TRUE.equals(isNew)) {
                log.info("Initialized request count for key {} to 0", requestCountKey);
                return;
            }

            // 获取当前值并验证
            String current = (String) redis.get(requestCountKey);
            if (current == null || !current.matches("\\d+")) {
                log.warn("Invalid value detected, resetting counter for key {}", requestCountKey);
                redis.set(requestCountKey, "0", 1, TimeUnit.MINUTES);
                return;
            }

            // 手动递增并存储（避免依赖 increment 方法）
            long newCount = Long.parseLong(current) + 1;
            redis.set(requestCountKey, String.valueOf(newCount), 1, TimeUnit.MINUTES);
            log.info("Manual increment success for key {}: {}", requestCountKey, newCount);

        } catch (Exception e) {
            log.error("Critical error handling counter for key {}", requestCountKey, e);
            // 异常时强制重置计数器
            redis.set(requestCountKey, "0", 1, TimeUnit.MINUTES);
        }
    }




    /**
     * 将验证码存储到 Redis
     *
     * @return
     */
    private Result storeCaptchaInRedis(String key, String code) {
        if (code == null || code.isEmpty()) {
            return Result.error("验证码不能为空");
        }
        // 确保值被序列化存储
        redis.set("captcha:" + key, code, CAPTCHA_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return null;
    }

    /**
     * 生成随机验证码
     */
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成验证码图片
     */
    private BufferedImage generateImage(String code) {
        int width = 120, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }

        // 绘制验证码
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 30);
        }

        g.dispose();
        return image;
    }

    /**
     * 将图片转换为字节数组
     */
    private byte[] convertImageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}
