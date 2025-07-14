package com.feign.pojo;


import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件名: ProductEs
 * 创建者: @一茶
 * 创建时间:2025/4/29 17:30
 * 描述：Es返回数据实体类
 * @Document() :  索引
 *      indexName : 索引名
 *      createIndex : 是否创建索引
 * @Id : 主键ID
 * @Field() : 字段
 *      type : 字段类型
 *      analyzer : 分词器
 *      format : 日期格式
 *      includeInParent : 是否包含在父文档中
 *      includeInRoot : 是否包含在根文档中
 *      nested : 是否嵌套
 *      index : 是否索引
 *      store : 是否存储
 *      searchAnalyzer : 搜索分词器
 *      searchAnalyzer : 搜索分词器
 *      copyTo : 拷贝到其他字段中
 *      ignoreAbove : 忽略超过指定长度的字段
 *      ignoreMalformed : 忽略格式错误的字段
 *      nullValue : 空值
 *      pattern : 日期格式
 *      precisionStep : 精度步长
 *      suffixes : 后缀
 *      tokenizer : 分词器
 *      type : 字段类型
 *      value : 默认值
 */
public class ProductEs {
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
    private List<ProductSkuEs> skus;

    public ProductEs() {
    }

    public ProductEs(Integer productId, Integer merchantId, Integer categoryId, String productName, String description, String status, Integer salesCount, Integer isRecommend, Integer sortOrder , String mainImage, LocalDateTime createdTime, LocalDateTime updatedTime, List<ProductSkuEs> skus) {
        this.productId = productId;
        this.merchantId = merchantId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.status = status;
        this.salesCount = salesCount;
        this.isRecommend = isRecommend;
        this.sortOrder = sortOrder;
        this.mainImage = mainImage;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.skus = skus;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<ProductSkuEs> getSkus() {
        return skus;
    }

    public void setSkus(List<ProductSkuEs> skus) {
        this.skus = skus;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }
}
