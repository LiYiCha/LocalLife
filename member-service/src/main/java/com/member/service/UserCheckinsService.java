package com.member.service;

import com.core.utils.Result;
import com.member.pojo.UserCheckins;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户签到记录表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface UserCheckinsService extends IService<UserCheckins> {

    @Transactional
    Result checkin(Integer userId);

    Result getCheckinInfo(Integer userId);
}
