package com.member.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件名: MerchantDTO
 * 创建者: @一茶
 * 创建时间:2025/5/13 17:00
 * 描述：
 */
@Data
public class MerchantDTO {
    private Integer merchantId;
    private String email;
    private String mobile;
    private String businessName;
    private String businessCategory;
    private String businessLicense;
    private String avatarUrl;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<MerchantStoresDTO> stores;
}
