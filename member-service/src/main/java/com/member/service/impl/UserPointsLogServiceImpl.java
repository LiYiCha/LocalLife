package com.member.service.impl;

import com.member.pojo.UserPointsLog;
import com.member.mapper.UserPointsLogMapper;
import com.member.service.UserPointsLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

/**
 * <p>
 * 用户积分明细表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Service
public class UserPointsLogServiceImpl extends ServiceImpl<UserPointsLogMapper, UserPointsLog> implements UserPointsLogService {
    @Resource
    private UserPointsLogMapper userPointsLogMapper;

    /**
     * 定时任务：每周一凌晨清理过期的积分记录,只保留本周积分记录
     */
    @Scheduled(cron = "0 0 0 * * MON") // 每周一凌晨执行
    public void cleanOldLogs() {
        userPointsLogMapper.cleanOldLogs();
    }
}
