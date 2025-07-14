package com.member.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.member.pojo.UserPointsLog;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文件名: CheckinsDTO
 * 创建者: @一茶
 * 创建时间:2025/4/17 9:24
 * 描述：签到信息（只记录7天）
 */
@Data
public class CheckinsDTO implements Serializable {
    private Integer userId;
    private Date checkinDate;
    private Short consecutiveDays;
    List<UserPointsLogDTO> pointsLog;
}
