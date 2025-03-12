package com.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {
    private Integer productId; // 需要与数据库字段类型一致
    private Integer quantity;
}
