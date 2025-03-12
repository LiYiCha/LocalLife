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
@RequestMapping("/userCheckins")
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
    @PostMapping("/{userId}")
    public Result checkin(@PathVariable("userId") Integer userId) {
        Result checkin = ucs.checkin(userId);
        if (checkin.getCode() != 200) {
            return checkin;
        }
        // 更新用户积分，签到奖励的积分值(1-签到，2-消费，3-评价）
        boolean pointsUpdated = us.updateUserPoints(userId, sign_reward, (byte) 1, "签到奖励");
        if (!pointsUpdated) {
            return Result.error("积分获取失败!");
        }
        return checkin;
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
            return Result.error();
        }
        return Result.success();
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
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 根据用户id查询签到记录
     *
     * @param userId
     * @return
     */
    @PostMapping("/get")
    public Result get(@RequestParam("userId") Integer userId) {
        QueryWrapper<UserCheckins> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        List<UserCheckins> checkins = ucs.list(qw);
        if (checkins == null) {
            return Result.error();
        }
        return Result.success(checkins);
    }
}
