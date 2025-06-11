package com.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Integer productId;
    private Integer merchantId;
    private Integer categoryId;
    private String productName;
    private String description;
    private String status;
    private Integer salesCount;
    private Integer isRecommend;
    private Integer sortOrder;
    private String mainImage;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private List<ProductImageDTO> images;
    private List<ProductSkuDTO> skus;

}
