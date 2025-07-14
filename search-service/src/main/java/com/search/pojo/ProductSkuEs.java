package com.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * 文件名: ProductSkuEs
 * 创建者: @一茶
 * 创建时间:2025/4/29 17:36
 * 描述：
 */
public class ProductSkuEs {
    @Id
    private Integer skuId;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String skuName;
    @Field(type = FieldType.Double) // 或 Keyword 保留精度
    private BigDecimal price;
    @Field(type = FieldType.Integer)
    private Integer stockQuantity;
    @Field(type = FieldType.Text)
    private String imageUrl;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
