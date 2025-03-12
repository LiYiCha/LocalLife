package com.feign.dto;

import lombok.Data;

@Data
public class ProductStockDTO {
    private Integer productId;  // 商品ID
    private Integer quantity;  // 数量

    public ProductStockDTO(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
