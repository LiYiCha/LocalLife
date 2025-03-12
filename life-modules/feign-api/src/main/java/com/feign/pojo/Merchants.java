package com.feign.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class Merchants implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    private Integer merchantId;

    /**
     * 商家用户名
     */
    private String username;

    /**
     * 密码哈希
     */
    private String password;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 商家名称
     */
    private String businessName;

    /**
     * 商家所属行业类别
     */
    private String businessCategory;

    /**
     * 营业执照编号
     */
    private String businessLicense;

    /**
     * 商家头像URL
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
