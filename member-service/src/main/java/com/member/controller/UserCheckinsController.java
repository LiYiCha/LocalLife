package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.member.pojo.UserCheckins;
import com.member.service.UserCheckinsService;
import com.core.utils.Result;
import com.member.service.UsersService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户签到记录表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/api/member/userCheckins")
public class UserCheckinsController {

    @Resource
    private UserCheckinsService ucs;
    @Autowired
    private UsersService us;
    private static int sign_reward = 1; // 签到奖励的积分值
    /**
     * 添加签到记录
     *
     * @param userId
     * @return
     */
    @PostMapping("/add")
    public Result checkin(@RequestParam("userId") Integer userId) {
        return ucs.checkin(userId);
    }

    /**
     * 修改签到记录
     *
     * @param checkin
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody UserCheckins checkin) {
        boolean flag = ucs.updateById(checkin);
        if (!flag) {
            return Result.error("签到记录修改失败!");
        }
        return Result.success("签到记录修改成功!");
    }

    /**
     * 删除签到记录
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        boolean flag = ucs.removeById(id);
        if (!flag) {
            return Result.error("签到记录删除失败!");
        }
        return Result.success("签到记录删除成功!");
    }

    /**
     * 根据用户id查询签到记录
     *
     * @param userId
     * @return
     */
    @PostMapping("/get")
    public Result get(@RequestParam("userId") Integer userId) {
        return  ucs.getCheckinInfo(userId);
    }
}
