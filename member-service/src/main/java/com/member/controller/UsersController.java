package com.member.controller;

import com.core.utils.MD5Utils;
import com.member.dto.UsersDTO;
import com.member.pojo.Users;
import com.member.service.UsersService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService us;

    /**
     * 添加用户
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestParam("username") String username,
                      @RequestParam("password") String password) {
        UsersDTO usersdto = us.getUserInfo(username);
        if (usersdto != null) {
            return Result.error("用户名已存在！");
        }
        // 密码加密加盐
        String newPassword = MD5Utils.encrypt(password);
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(newPassword);
        // 保存用户信息
        boolean flag = us.save(users);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        UsersDTO usersdto = us.getUserInfo(username);
        if (usersdto == null){
            return Result.error("账号不存在！");
        }
        // 密码加密加盐
        Users user = us.getById(usersdto.getUserId());
        String newPassword = MD5Utils.encrypt(password);
        if (!user.getPassword().equals(newPassword)) {
            return Result.error("密码错误！");
        }
        return Result.success();
    }
    /**
     * 更新用户信息
     * @param user
     * @return
     */

    @PostMapping("/update")
    public Result update(@RequestBody Users user) {
        boolean flag = us.updateById(user);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 注销用户
     * @param username
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("uaername") String username) {
        boolean flag = us.deleteUser(username);
        if (!flag) {
            return Result.error("注销失败！");
        }
        return Result.success();
    }


    /**
     * 获取用户信息
     * @param username
     * @return
     */
    @PostMapping("/get")
    public Result get(@RequestParam("username") String username) {
        UsersDTO users = us.getUserInfo(username);
        if (users == null) {
            return Result.error();
        }
        return Result.success(users);
    }
}
