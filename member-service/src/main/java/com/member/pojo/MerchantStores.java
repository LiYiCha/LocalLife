package com.member.pojo;

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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 商家店铺信息表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("merchant_stores")
public class MerchantStores implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    /**
     * 关联到 merchants 表中的商家
     */
    @TableField("merchant_id")
    private Integer merchantId;

    /**
     * 店铺名称
     */
    @TableField("name")
    private String name;

    /**
     * 店铺描述
     */
    @TableField("description")
    private String description;

    /**
     * 店铺logoURL
     */
    @TableField("logo_url")
    private String logoUrl;

    /**
     * 店铺地址
     */
    @TableField("address")
    private String address;

    /**
     * 营业日1-7
     */
    @TableField("day_of_week")
    private Byte dayOfWeek;

    /**
     * 营业时间
     */
    @TableField("opening_time")
    private java.sql.Time openingTime;

    /**
     * 关闭时间
     */
    @TableField("closing_time")
    private java.sql.Time closingTime;

    /**
     * 状态
     */
    @TableField("status")
    private Byte status; // 0: 正常营业, 1: 暂停营业, 2: 关闭

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

}
