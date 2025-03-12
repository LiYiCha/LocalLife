package com.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.member.dto.UsersDTO;
import com.member.pojo.*;
import com.member.mapper.UsersMapper;
import com.member.service.UsersService;
import com.member.service.AddressesService;
import com.member.service.UserCheckinsService;
import com.member.service.UserFavoritesService;
import com.member.service.UserPointsLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Resource
    private UsersMapper usersMapper;
    @Autowired
    private AddressesService addressesService;

    @Autowired
    private UserCheckinsService userCheckinsService;

    @Autowired
    private UserFavoritesService userFavoritesService;

    @Autowired
    private UserPointsLogService userPointsLogService;

    /**
     * 删除用户账号及其相关信息
     */
    @Override
    public boolean deleteUser(String username) {
        // 检查用户是否存在
        UsersDTO user = getUserInfo(username);
        Integer userId = user.getUserId();
        if (user == null) {
            return false; // 用户不存在
        }
        try {
            // 删除用户的地址信息
            addressesService.remove(new QueryWrapper<Addresses>().eq("user_id", userId));
            // 删除用户的签到记录
            userCheckinsService.remove(new QueryWrapper<UserCheckins>().eq("user_id", userId));
            // 删除用户的收藏信息
            userFavoritesService.remove(new QueryWrapper<UserFavorites>().eq("user_id", userId));
            // 删除用户的积分明细
            userPointsLogService.remove(new QueryWrapper<UserPointsLog>().eq("user_id", userId));
            // 删除用户账号
            return this.removeById(userId);
        } catch (Exception e) {
            // 记录异常日志
            // logger.error("删除用户失败", e);
            return false;
        }
    }

    @Override
    public UsersDTO getUserInfo(String username) {
        UsersDTO userdto = usersMapper.getUsersInfo(username);
        if (userdto == null) {
            return null;
        }
        return userdto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPoints(Integer userId, Integer points, Byte type, String source) {
        // 获取当前用户的积分
        Users user = getById(userId);
        if (user == null) {
            return false;
        }

        // 更新用户的积分
        user.setTotalPoints(user.getTotalPoints() + points);
        //更新用户成长值
        user.setTotalGrowthValue(user.getTotalGrowthValue() + points);
        boolean updated = updateById(user);
        if (!updated) {
            return false;
        }

        // 记录积分变动明细
        UserPointsLog pointsLog = new UserPointsLog();
        pointsLog.setUserId(userId);
        pointsLog.setPoints(points);
        pointsLog.setType(type);
        pointsLog.setSource(source);

        // 保存积分变动明细到数据库
        return userPointsLogService.save(pointsLog);
    }

}
