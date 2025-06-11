package com.member.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用户签到记录表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_checkins")
public class UserCheckins implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签到记录ID
     */
    @TableId(value = "checkin_id", type = IdType.AUTO)
    private Long checkinId;

    /**
     * 关联到 users 表中的用户
     */
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 签到日期
     */
    @TableField("checkin_date")
    @NotNull(message = "签到日期不能为空")
    private Date checkinDate;

    /**
     * 连续签到天数
     */
    @TableField("consecutive_days")
    @NotNull(message = "连续签到天数不能为空")
    private Short consecutiveDays;

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
