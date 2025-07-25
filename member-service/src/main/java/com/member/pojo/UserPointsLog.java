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
 * 用户积分明细表
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_points_log")
public class UserPointsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 积分变动数量
     */
    @TableField("points")
    @NotNull(message = "积分数量不能为空")
    private Integer points;

    /**
     * 积分类型：1-签到，2-消费，3-评价
     */
    @TableField("type")
    @NotNull(message = "积分类型不能为空")
    private Byte type;

    /**
     * 积分来源描述
     */
    @TableField("source")
    @NotBlank(message = "积分来源描述不能为空")
    private String source;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

}
