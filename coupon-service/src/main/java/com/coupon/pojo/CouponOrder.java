package com.coupon.pojo;

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
 * 优惠券订单表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("coupon_order")
public class CouponOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 下单的用户id
     */
    @TableField("user_id")
    private int userId;

    /**
     * 购买的代金券id
     */
    @TableField("coupon_id")
    private int couponId;

    /**
     * 订单状态，1：未支付；2：已支付；3：已核销；4：已取消
     */
    @TableField("status")
    private Byte status;

    /**
     * 下单时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private LocalDateTime payTime;

    /**
     * 核销时间
     */
    @TableField("use_time")
    private LocalDateTime useTime;
}
