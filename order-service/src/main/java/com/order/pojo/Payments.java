package com.order.pojo;

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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 支付记录表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("payments")
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录ID
     */
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Integer paymentId;

    /**
     * 关联到 orders 表
     */
    @TableField("order_id")
    private Integer orderId;

    /**
     * 支付流水号
     */
    @TableField("payment_no")
    private String paymentNo;

    /**
     * 支付金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 支付状态
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
