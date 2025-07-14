package com.coupon.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.coupon.pojo.CouponOrder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件名: CouponDetailDTO
 * 创建者: @一茶
 * 创建时间:2025/4/15 17:57
 * 描述：
 */
@Data
public class CouponDetailDTO implements Serializable {
    private Integer couponId;
    private Integer storeId;
    private String title;
    private String description;
    private Integer payValue;
    private Integer actualValue;
    private Byte type;
    private Byte scope;
    private Integer categoryId;
    private Integer productId;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private List<CouponOrderDTO> couponOrderDTOList;

}
