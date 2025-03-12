package com.member.pojo;

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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 地址信息表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("addresses")
public class Addresses implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;

    /**
     * 关联到 users 表中的用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收货人姓名
     */
    @TableField("name")
    private String name;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 省份/直辖市
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @TableField("region")
    private String region;

    /**
     * 详细地址(街道)
     */
    @TableField("detail_address")
    private String detailAddress;

    /**
     * 是否默认地址
     */
    @TableField("is_default")
    private Byte isDefault;

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
