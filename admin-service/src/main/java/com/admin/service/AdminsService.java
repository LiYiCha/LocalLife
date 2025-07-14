package com.admin.service;

import com.admin.pojo.Admins;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.utils.Result;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-06-14
 */
public interface AdminsService extends IService<Admins> {

    Result login(String username, String password);
    Result register(String username, String password);
    Result getInfo(String username);
    Result getUserList();
    Result deleteUser(String username);
    Result updateUser(Admins admins);
    Result logout(String token);
    Result changePassword(String username, String oldPassword, String newPassword);
}
