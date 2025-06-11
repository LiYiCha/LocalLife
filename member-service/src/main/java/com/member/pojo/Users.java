package com.member.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码哈希
     */
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 手机号码
     */
    @TableField("mobile")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     * 名称
     */
    @TableField("nickname")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    /**
     * 邮箱
     */
    @TableField("email")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 头像
     */
    @TableField("header")
    @NotBlank(message = "头像不能为空")
    private String header;

    /**
     * 性别1女2男0保密
     */
    @TableField("gender")
    @NotBlank(message = "性别不能为空")
    private Byte gender;

    /**
     * 生日
     */
    @TableField("birth")
    @NotBlank(message = "生日不能为空")
    private LocalDateTime birth;

    /**
     * 个性签名
     */
    @TableField("sign")
    @NotBlank(message = "个性签名不能为空")
    private String sign;

    /**
     * 用户累计积分
     */
    @TableField("total_points")
    @NotBlank(message = "用户累计积分不能为空")
    private Long totalPoints;

    /**
     * 用户累计成长值
     */
    @TableField("total_growth_value")
    @NotBlank(message = "用户累计成长值不能为空")
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
