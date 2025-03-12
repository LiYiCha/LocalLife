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
 * 商家信息表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("merchants")
public class Merchants implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    @TableId(value = "merchant_id", type = IdType.AUTO)
    private Integer merchantId;

    /**
     * 商家用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码哈希
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱地址
     */
    @TableField("email")
    private String email;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 商家名称
     */
    @TableField("business_name")
    private String businessName;

    /**
     * 商家所属行业类别
     */
    @TableField("business_category")
    private String businessCategory;

    /**
     * 营业执照编号
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * 商家头像URL
     */
    @TableField("avatar_url")
    private String avatarUrl;

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
