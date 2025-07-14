package com.admin.controller;

import com.admin.pojo.Admins;
import com.admin.service.AdminsService;
import com.core.utils.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-06-14
 */
@RestController
@RequestMapping("/api/v1/admins")
public class AdminsController {

    private final AdminsService adminsService;

    public AdminsController(AdminsService adminsService) {
        this.adminsService = adminsService;
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password) {
        return adminsService.register(username, password);
    }
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        return adminsService.login(username, password);
    }


    /**
     * 登出
     *
     * @param token token
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result logout(@RequestParam(value = "token") String token) {
        return adminsService.logout(token);
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * {@code @response} {
     *     "code": 200,
     *     "msg": "操作成功",
     *     "data": {
     *         "adminId": 1,
     *         "username": "admin",
     *         "role": "管理员"
     *     }
     * }
     */
    @PostMapping("/getInfo")
    public Result getInfo(@RequestParam(value = "username") String username) {
        return adminsService.getInfo(username);
    }

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @PostMapping("/changePassword")
    public Result changePassword(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "oldPassword") String oldPassword,
                                 @RequestParam(value = "newPassword") String newPassword) {
        return adminsService.changePassword(username, oldPassword, newPassword);
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @PostMapping("/getUserList")
    public Result getUserList() {
        // 调用adminsService的getUserList方法，获取用户列表
        return adminsService.getUserList();
    }

    /**
     * 删除用户
     *
     * @param username 用户名
     * @return 删除结果
     */
    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestParam(value = "username") String username) {
        // 调用adminsService的deleteUser方法，传入用户名，返回删除结果
        return adminsService.deleteUser(username);
    }

    /**
     * 更新用户信息
     * @param admins 用户实体类
     * @return 更新结果
     */
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody Admins admins) {
        // 调用adminsService的updateUser方法，更新用户信息
        return adminsService.updateUser(admins);
    }
}
