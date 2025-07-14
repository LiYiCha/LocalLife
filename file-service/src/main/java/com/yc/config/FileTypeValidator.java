package com.yc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件魔数是文件头部的一段特定字节序列，用于标识文件的真实类型。
 */
public class FileTypeValidator {

    // 定义常见文件类型的魔数映射
    private static final Map<String, byte[]> FILE_TYPE_MAP = new HashMap<>();

    static {
        FILE_TYPE_MAP.put("jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
        FILE_TYPE_MAP.put("png", new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47});
        FILE_TYPE_MAP.put("gif", new byte[]{(byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38});
        // 其他文件类型
    }

    /**
     * 校验文件类型是否匹配
     */
    public static boolean isValidFileType(InputStream inputStream, String expectedType) throws IOException {
        byte[] magicBytes = FILE_TYPE_MAP.get(expectedType.toLowerCase());
        if (magicBytes == null) {
            throw new IllegalArgumentException("Unsupported files type: " + expectedType);
        }

        byte[] fileHeader = new byte[magicBytes.length];
        int bytesRead = inputStream.read(fileHeader);

        // 将流重置到起始位置（如果支持）
        if (inputStream.markSupported()) {
            inputStream.reset();
        }

        if (bytesRead != magicBytes.length) {
            return false; // 文件头读取失败
        }

        for (int i = 0; i < magicBytes.length; i++) {
            if (fileHeader[i] != magicBytes[i]) {
                return false; // 魔数不匹配
            }
        }
        return true;
    }
}
