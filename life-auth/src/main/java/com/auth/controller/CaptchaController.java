package com.auth.controller;

import com.core.utils.RedisUtil;
import com.core.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private RedisUtil redis;

    /**
     * 生成验证码(arrayBuffer格式响应）
     * @param key
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateCaptcha(@RequestParam("key") String key, HttpServletRequest request) throws IOException {
        String ipAddress = request.getRemoteAddr();
        String requestCountKey = "captcha:requestCount:" + ipAddress;
        String ipLimitKey = "captcha:ipLimit:" + ipAddress;

        // 检查IP是否被限制
        if (redis.hasKey(ipLimitKey)) {
            return ResponseEntity.status(429).body("请求过于频繁，请稍后再试".getBytes());
        }

        // 获取当前请求次数
        String requestCountStr = (String) redis.get(requestCountKey);
        Long requestCount = (requestCountStr != null) ? Long.parseLong(requestCountStr) : 0L;


        // 如果请求次数超过5次，限制该IP十分钟
        if (requestCount >= 5) {
            redis.set(ipLimitKey, "true", 10, TimeUnit.MINUTES);
            return ResponseEntity.status(429).body("请求过于频繁，请稍后再试".getBytes());
        }

        // 增加请求次数
        redis.set(requestCountKey, requestCount + 1, 1, TimeUnit.MINUTES);

        // 生成随机验证码
        String code = generateRandomCode(4);

        // 将验证码存入Redis，设置过期时间为5分钟
        redis.set("captcha:" + key, code, 5, TimeUnit.MINUTES);

        // 生成验证码图片
        BufferedImage image = generateImage(code);

        // 将图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        // 返回图片
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }






    /**
     * 验证验证码
     * @param key
     * @param code
     * @return
     */
    @PostMapping("/verify")
    public Result verifyCaptcha(@RequestParam("key") String key,
                                @RequestParam("code") String code) {
        String redisCaptcha = (String) redis.get("captcha:" + key);
        //key转成大写
        code = code.toUpperCase();
        // 去除首尾空白字符，并统一转换为小写进行比较
        if (redisCaptcha.equals(code)) {
            return Result.success();
        }
        return Result.error("无效验证码");
    }



    // 生成随机验证码
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // 生成验证码图片
    private BufferedImage generateImage(String code) {
        int width = 120;
        int height = 40;
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
}
