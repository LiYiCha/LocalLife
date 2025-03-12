package com.feign.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Data
@Accessors(chain = true)
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
