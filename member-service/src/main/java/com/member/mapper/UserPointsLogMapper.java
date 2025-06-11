package com.member.mapper;

import com.member.pojo.UserPointsLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户积分明细表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface UserPointsLogMapper extends BaseMapper<UserPointsLog> {

    void cleanOldLogs();
}
