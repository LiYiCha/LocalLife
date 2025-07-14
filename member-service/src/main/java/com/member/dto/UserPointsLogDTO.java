package com.member.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 文件名: UserPointsLogDTO
 * 创建者: @一茶
 * 创建时间:2025/4/17 9:27
 * 描述：
 */
@Data
public class UserPointsLogDTO {
    private Integer points;
    private Byte type;
    private String source;
    private LocalDateTime createdTime;
}
