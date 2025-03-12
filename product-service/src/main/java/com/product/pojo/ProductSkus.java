package com.product.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品SKU表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product_skus")
public class ProductSkus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    @TableId(value = "sku_id", type = IdType.AUTO)
    private Integer skuId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * SKU名称(如：红色/L号,大碗小碗)
     */
    @TableField("sku_name")
    private String skuName;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock_quantity")
    private Integer stockQuantity;

    /**
     * SKU图片
     */
    @TableField("image_url")
    private String imageUrl;

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
