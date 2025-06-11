package com.community.service;

import com.community.pojo.UsersPosts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.core.utils.Result;
import org.springframework.cache.annotation.Cacheable;

/**
 * <p>
 * 交流社区帖子表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface UsersPostsService extends IService<UsersPosts> {


    @Cacheable(value = "postsCache", key = "#title + '_' + #page + '_' + #size")
    Result searchPosts(String title, int page, int size);

    Result likePost(Integer postId);
}
