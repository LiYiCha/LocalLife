package com.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String SALT = "life_yicha_!@#$%^&*"; // 加盐增加安全性

    /**
     * 生成32位大写MD5加密字符串
     * @param password 原始密码
     * @return 加密后的字符串
     */
    public static String encrypt(String password) {
        try {
            // 加盐
            password = password + SALT;

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());

            // 将字节数组转换为16进制字符串
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    result.append("0");
                }
                result.append(hex);
            }

            // 再次MD5加密，增加安全性
            bytes = md.digest(result.toString().getBytes());
            result = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    result.append("0");
                }
                result.append(hex);
            }

            return result.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证密码是否正确
     * @param inputPassword 输入的密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String inputPassword, String encryptedPassword) {
        String encryptInput = encrypt(inputPassword);
        return encryptInput != null && encryptInput.equals(encryptedPassword);
    }
}
