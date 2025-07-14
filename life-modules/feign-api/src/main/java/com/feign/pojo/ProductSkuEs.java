package com.feign.pojo;

import java.math.BigDecimal;

/**
 * 文件名: ProductSkuEs
 * 创建者: @一茶
 * 创建时间:2025/4/29 17:36
 * 描述：
 */
public class ProductSkuEs {
    private Integer skuId;
    private String skuName;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;

    public ProductSkuEs() {
    }

    public ProductSkuEs(Integer skuId, String skuName, BigDecimal price, Integer stockQuantity, String imageUrl) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }

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
