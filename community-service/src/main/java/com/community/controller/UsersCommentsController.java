package com.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.pojo.UsersComments;
import com.community.service.UsersCommentsService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 交流社区评论表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */


@RestController
@RequestMapping("/api/community/comments")
public class UsersCommentsController {

    @Autowired
    private UsersCommentsService usersCommentsService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping("/add")
    public Result createComment(@RequestBody UsersComments comment) {
        return usersCommentsService.addComment(comment);
    }


    /**
     * 获取评论根据评论id
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public Result getCommentById(@RequestParam("id") Integer id) {
        UsersComments comment = usersCommentsService.getById(id);
        return comment != null ? Result.success(comment) : Result.error("评论不存在");
    }

    /**
     * 更新评论
     * @param comment
     * @return
     */
    @PostMapping("/update")
    public Result updateComment(@RequestBody UsersComments comment) {
        boolean success = usersCommentsService.updateById(comment);
        return success ? Result.success(comment) : Result.error("更新评论失败");
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @PostMapping("/delById")
    public Result deleteComment(@RequestParam("id") Integer id) {
        return usersCommentsService.removeById(id);
    }

    /**
     * 获取某个帖子的所有评论
     * @param postId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/post")
    public Result getCommentsByPost(
            @RequestParam("postId") Integer postId,
            @RequestParam(defaultValue = "1",name = "page") Integer page,
            @RequestParam(defaultValue = "10",name="size") Integer size) {

        Page<UsersComments> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UsersComments> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersComments::getPostId, postId)
                .orderByDesc(UsersComments::getCreatedTime);

        Page<UsersComments> result = usersCommentsService.page(pageParam, wrapper);
        return Result.success(result);
    }

    /**
     * 获取某个用户的所有评论
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/userAll")
    public Result getCommentsByUser(
            @RequestParam("userId") Integer userId,
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @RequestParam(defaultValue = "10", name = "size") Integer size) {

        Page<UsersComments> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UsersComments> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersComments::getUserId, userId)
                .orderByDesc(UsersComments::getCreatedTime);

        Page<UsersComments> result = usersCommentsService.page(pageParam, wrapper);
        return Result.success(result);
    }
}
