package com.feign.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码哈希
     */
    private String password;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 名称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String header;

    /**
     * 性别1女2男0保密
     */
    private Byte gender;

    /**
     * 生日
     */
    private LocalDateTime birth;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 用户累计积分
     */
    private Long totalPoints;

    /**
     * 用户累计成长值
     */
    private Long totalGrowthValue;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}
