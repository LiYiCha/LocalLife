package com.member.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码哈希
     */
    @TableField("password")
    private String password;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 名称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 头像
     */
    @TableField("header")
    private String header;

    /**
     * 性别1女2男0保密
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 生日
     */
    @TableField("birth")
    private LocalDateTime birth;

    /**
     * 个性签名
     */
    @TableField("sign")
    private String sign;

    /**
     * 用户累计积分
     */
    @TableField("total_points")
    private Long totalPoints;

    /**
     * 用户累计成长值
     */
    @TableField("total_growth_value")
    private Long totalGrowthValue;

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
