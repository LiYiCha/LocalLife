package com.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.utils.RedisUtil;
import com.core.utils.Result;
import com.member.mapper.UserCheckinsMapper;
import com.member.pojo.UserCheckins;
import com.member.service.UserCheckinsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserCheckinsServiceImpl extends ServiceImpl<UserCheckinsMapper, UserCheckins> implements UserCheckinsService {

    @Resource
    private RedisUtil redisUtil;

    private static final String CHECKIN_KEY_PREFIX = "user:checkin:";
    private static final String LOCK_KEY_PREFIX = "user:checkin:lock:";

    /**
     * 用户签到
     *
     * @param userId 用户ID
     */
    @Override
    @Transactional
    public Result checkin(Integer userId) {
        LocalDate today = LocalDate.now();
        String key = getCheckinKey(userId, today.getYear(), today.getMonthValue());
        int offset = today.getDayOfMonth() - 1; // Bitmap 的偏移量，从 0 开始

        // 加分布式锁，防止并发签到
        String lockKey = LOCK_KEY_PREFIX + userId;
        boolean locked = tryLock(lockKey, 10); // 锁超时时间为 10 秒
        if (!locked) {
            return Result.error("签到失败，请稍后重试");
        }

        try {
            // 检查是否已经签到
            Boolean hasCheckedIn = redisUtil.getBit(key, offset);
            if (hasCheckedIn != null && hasCheckedIn) {
                return Result.error("今日已签到");
            }

            // 设置签到状态
            redisUtil.setBit(key, offset, true);
            redisUtil.expire(key, 31, TimeUnit.DAYS); // 设置过期时间为 31 天

            // 更新数据库中的连续签到天数
            UserCheckins lastCheckin = getLastCheckin(userId);
            UserCheckins checkinRecord = new UserCheckins();
            checkinRecord.setUserId(userId);
            checkinRecord.setCheckinDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            // 判断是否是连续签到，更新连续天数
            if (lastCheckin != null && lastCheckin.getCheckinDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).isEqual(today)) {
                checkinRecord.setConsecutiveDays((short) (lastCheckin.getConsecutiveDays() + 1));
            } else {
                // 如果连续签到天数为 1，则重置为 1
                checkinRecord.setConsecutiveDays((short) 1);
            }

            // 保存到数据库
            boolean save = save(checkinRecord);
            if (!save) {
                return Result.error("签到失败");
            }
            return Result.success();
        } finally {
            // 释放锁
            if (locked) {
                releaseLock(lockKey);
                System.out.println("锁已释放，lockKey: " + lockKey);
            }
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁的 Key
     * @param timeout 锁的超时时间（秒）
     * @return 是否加锁成功
     */
    private boolean tryLock(String lockKey, int timeout) {
        System.out.println("尝试获取锁，lockKey: " + lockKey);
        // 使用 setIfAbsent 方法设置锁，并同时设置过期时间
        boolean success = redisUtil.setIfAbsent(lockKey, "locked", timeout, TimeUnit.SECONDS);
        System.out.println("setIfAbsent 返回值: " + success);
        return success;
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的 Key
     */
    private void releaseLock(String lockKey) {
        System.out.println("释放锁，lockKey: " + lockKey);
        redisUtil.delete(lockKey);
    }

    /**
     * 获取用户最后一次签到记录
     *
     * @param userId 用户ID
     * @return 最后一次签到记录
     */
    private UserCheckins getLastCheckin(Integer userId) {
        return getOne(
                new LambdaQueryWrapper<UserCheckins>()
                        .eq(UserCheckins::getUserId, userId)
                        .orderByDesc(UserCheckins::getCheckinDate)
                        .last("LIMIT 1")
        );
    }

    /**
     * 生成 Redis Key
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     * @return Redis Key
     */
    private String getCheckinKey(Integer userId, int year, int month) {
        return CHECKIN_KEY_PREFIX + userId + ":" + year + ":" + month;
    }
}
