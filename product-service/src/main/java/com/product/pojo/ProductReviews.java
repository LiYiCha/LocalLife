package com.product.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品评价表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("product_reviews")
public class ProductReviews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "review_id", type = IdType.AUTO)
    private Long reviewId;

    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Integer orderId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 评分(1-5星)
     */
    @TableField("rating")
    private Byte rating;

    /**
     * 评价内容
     */
    @TableField("content")
    private String content;

    /**
     * 商家回复
     */
    @TableField("reply")
    private String reply;

    /**
     * 回复时间
     */
    @TableField("reply_time")
    private LocalDateTime replyTime;

    /**
     * 状态：0-隐藏，1-显示
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
