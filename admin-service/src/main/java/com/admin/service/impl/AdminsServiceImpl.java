package com.admin.service.impl;

import com.admin.pojo.Admins;
import com.admin.mapper.AdminsMapper;
import com.admin.service.AdminsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.utils.Result;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-06-14
 */
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public Admins getByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return 登录结果
     */
    @Override
    public Result login(String username, String password) {
        Admins admins = getByUsername(username);
        if (admins == null) {
             return Result.error("用户不存在");
         } else if (!admins.getPassword().equals(password)) {
             return Result.error("密码错误");
         }
         return Result.success(admins);
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return 注册结果
     */
    @Override
    public Result register(String username, String password) {
        Admins admins = getByUsername(username);
        if (admins != null) {
            return Result.error("用户已经存在");
        }
        Admins newAdmins = new Admins();
        newAdmins.setUsername(username);
        newAdmins.setPassword(password);
        baseMapper.insert(newAdmins);
        return Result.success("注册成功");
    }

    /**
     * 获取用户信息
     * @param username
     * @return 用户信息
     */
    @Override
    public Result getInfo(String username) {
        Admins admins = getByUsername(username);
        return Result.success(admins);
    }

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @Override
    public Result getUserList() {
        // 获取用户列表
        return null;
    }

    /**
     * 删除用户
     * @param username
     * @return 删除结果
     */
    @Override
    public Result deleteUser(String username) {
        Admins admins = getByUsername(username);
        if (admins == null) {
            return Result.error("用户不存在");
        }
        int i = baseMapper.deleteById(admins.getAdminId());
        if (i == 1) {
            return Result.success("删除用户成功");
        }else {
            return Result.error("删除用户失败");
        }
    }

    /**
     * 更新用户信息
     * @param admins
     * @return 更新结果
     */
    @Override
    public Result updateUser(Admins admins) {
        if (admins == null) {
            return Result.error("用户不存在");
        }
        int i = baseMapper.updateById(admins);
        if (i == 1) {
            return Result.success("更新用户成功");
        }else {
            return Result.error("更新用户失败");
        }
    }

    /**
     * 登出
     * @param token
     * @return 登出结果
     */
    @Override
    public Result logout(String token) {
        // 删除token
        return null;
    }

    /**
     * 修改密码根据旧密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return 修改密码结果
     */
    @Override
    public Result changePassword(String username, String oldPassword, String newPassword) {
        Admins admins = getByUsername(username);
        if (admins == null) {
            return Result.error("用户不存在");
        }
        if (!admins.getPassword().equals(oldPassword)) {
            return Result.error("旧密码错误");
        }
        admins.setPassword(newPassword);
        int i = baseMapper.updateById(admins);
        if (i == 1) {
            return Result.success("修改密码成功");
        }else {
            return Result.error("修改密码失败");
        }
    }
    //忘记密码/重新设置密码
}
