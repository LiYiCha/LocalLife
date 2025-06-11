package com.community.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.pojo.UsersPosts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 交流社区帖子表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface UsersPostsMapper extends BaseMapper<UsersPosts> {

    IPage<UsersPosts> search(Page<UsersPosts> page, @Param("title") String title);

    int likePost(@Param("postId")Integer postId);
    int incrementComments(Integer postId);
    int decrementComments(Integer postId);
}
