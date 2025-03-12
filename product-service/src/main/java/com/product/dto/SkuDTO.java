package com.product.dto;

import lombok.Data;

@Data
public  class SkuDTO {
    private Integer skuId;
    private String skuName;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl;
}
