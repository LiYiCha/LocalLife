package com.member.dto;

import lombok.Data;

/**
 * 文件名: MerchantStoresDTO
 * 创建者: @一茶
 * 创建时间:2025/5/13 17:03
 * 描述：
 */
@Data
public class MerchantStoresDTO {
    private Integer storeId;
    private String storeName;
    private String description;
    private String logoUrl;
    private String address;
    private Byte dayOfWeek;
    private java.sql.Time openingTime;
    private java.sql.Time closingTime;
    private Byte status; // 0: 正常营业, 1: 暂停营业, 2: 关闭
}
