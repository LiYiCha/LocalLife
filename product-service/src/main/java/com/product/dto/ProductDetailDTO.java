package com.product.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDetailDTO {
    private Integer productId;
    private String productName;
    private String description;
    private String status;
    private Integer salesCount;
    private Boolean isRecommend;
    private Integer sortOrder;
    private List<String> imageUrls; // 商品图片列表
    private List<SkuDTO> skus; // 商品SKU列表

}
