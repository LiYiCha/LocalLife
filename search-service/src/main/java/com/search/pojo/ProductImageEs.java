package com.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 文件名: ProductImageEs
 * 创建者: @一茶
 * 创建时间:2025/4/29 17:36
 * 描述：
 */
public class ProductImageEs {
    @Id
    private Integer imageId;
    @Field(type = FieldType.Text)
    private String imageUrl;
    @Field(type = FieldType.Integer)
    private Integer isMain;
    @Field(type = FieldType.Integer)
    private Integer sortOrder;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
