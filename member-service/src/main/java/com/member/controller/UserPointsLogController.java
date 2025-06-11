package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.member.pojo.UserPointsLog;
import com.member.service.UserPointsLogService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户积分明细表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/api/member/userPointsLog")
public class UserPointsLogController {

    @Autowired
    private UserPointsLogService upls;

    /**
     * 添加积分明细
     *
     * @param pointsLog
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody UserPointsLog pointsLog) {
        boolean flag = upls.save(pointsLog);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }


    /**
     * 删除积分明细
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        boolean flag = upls.removeById(id);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 根据用户id查询积分明细
     *
     * @param userId
     * @return
     */
    @PostMapping("/get")
    public Result get(@RequestParam("userId") Integer userId) {
        QueryWrapper<UserPointsLog> qw = new QueryWrapper<>();
        qw.eq("user_id", userId)
                .orderByDesc("created_time"); // 按日期升降排序
        List<UserPointsLog> pointsLogs = upls.list(qw);
        if (pointsLogs == null || pointsLogs.isEmpty()) {
            return Result.error();
        }
        // 只保留最新的5条记录
        if (pointsLogs.size() > 5) {
            for (int i = 5; i < pointsLogs.size(); i++) {
                upls.removeById(pointsLogs.get(i).getId());
            }
            pointsLogs = pointsLogs.subList(0, 5);
        }
        return Result.success(pointsLogs);
    }

}
