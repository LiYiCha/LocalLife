package com.product.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    /**
     * 商家ID
     */
    @TableField("merchant_id")
    private Integer merchantId;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Integer categoryId;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品描述
     */
    @TableField("description")
    private String description;

    /**
     * 商品状态
     */
    @TableField("status")
    private String status;

    /**
     * 销量
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 是否推荐
     */
    @TableField("is_recommend")
    private Boolean isRecommend;

    /**
     * 排序权重
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 主图片
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
