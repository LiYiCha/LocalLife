package com.member.mapper;

import com.member.dto.CheckinsDTO;
import com.member.pojo.UserCheckins;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户签到记录表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface UserCheckinsMapper extends BaseMapper<UserCheckins> {

    CheckinsDTO getCheckinInfo(Integer userId);
}
