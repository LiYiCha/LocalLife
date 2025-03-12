package com.member.mapper;

import com.member.dto.UsersDTO;
import com.member.pojo.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface UsersMapper extends BaseMapper<Users> {
    UsersDTO getUsersInfo(String username);

}
