package com.member.service.impl;

import com.member.pojo.UserFavorites;
import com.member.mapper.UserFavoritesMapper;
import com.member.service.UserFavoritesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@Service
public class UserFavoritesServiceImpl extends ServiceImpl<UserFavoritesMapper, UserFavorites> implements UserFavoritesService {

}
