package com.coupon.pojo;

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
 * 优惠券表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 商铺id
     */
    @TableField("store_id")
    private int storeId;

    /**
     * 代金券标题
     */
    @TableField("title")
    private String title;

    /**
     * 支付金额，单位是分
     */
    @TableField("pay_value")
    private int payValue;

    /**
     * 抵扣金额，单位是分
     */
    @TableField("actual_value")
    private int actualValue;

    /**
     * 优惠券类型，1-满减券；2-折扣券；3-无门槛券(立减卷)
     */
    @TableField("type")
    private Byte type;
    /**
     * 优惠范围，1-全场通用；2-指定分类；3-指定商品
     */
    @TableField("scope")
    private Byte scope;
    /**
     * 指定分类id
     */
    @TableField("category_id")
    private int categoryId;
    /**
     * 指定商品id
     */
    @TableField("product_id")
    private int productId;
    /**
     * 总库存
     */
    @TableField("total_stock")
    private Integer totalStock;

    /**
     * 可用库存
     */
    @TableField("available_stock")
    private Integer availableStock;

    /**
     * 每人限购数量
     */
    @TableField("limit_per_user")
    private Integer limitPerUser;

    /**
     * 有效期开始时间
     */
    @TableField("valid_start_time")
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    @TableField("valid_end_time")
    private LocalDateTime validEndTime;

    /**
     * 1,上架; 2,下架; 3,过期
     */
    @TableField("status")
    private Byte status;

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
