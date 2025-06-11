package com.community.service.impl;

import com.community.mapper.UsersPostsMapper;
import com.community.pojo.UsersComments;
import com.community.mapper.UsersCommentsMapper;
import com.community.service.UsersCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.core.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 交流社区评论表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class UsersCommentsServiceImpl extends ServiceImpl<UsersCommentsMapper, UsersComments> implements UsersCommentsService {


    @Resource
    private UsersPostsMapper usersPostsMapper;

    /**
     * Add comment and update post comment count
     *
     * @param comment
     * @return
     */
    @Override
    public Result addComment(UsersComments comment) {
        boolean save = this.save(comment);
        if (save) {
            // Update post comment count
            usersPostsMapper.incrementComments(comment.getPostId());
            return Result.success("评论成功");
        }
        return Result.error("评论失败");
    }


    /**
     * Delete comment and update post comment count
     *
     * @param id
     * @return
     */
    @Override
    public Result removeById(Integer id) {
        UsersComments comment = this.getById(id);
        if (comment != null) {
            // Update post comment count before deletion
            boolean result = super.removeById(id);
            if (result) {
                usersPostsMapper.decrementComments(comment.getPostId());
            }
            return Result.success();
        }
        return Result.error("评论不存在");
    }
}
