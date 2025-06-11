package com.member.service;

import com.member.dto.UsersDTO;
import com.member.pojo.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
public interface UsersService extends IService<Users> {

    boolean deleteUser(String username);

    UsersDTO getUserInfo(String username);

    @Transactional(rollbackFor = Exception.class)
    boolean updateUserPoints(Integer userId, Integer points, Byte type, String source);
}
