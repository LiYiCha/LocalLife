package com.community.controller;

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
@RequestMapping("/comments")
public class UsersCommentsController {

    @Autowired
    private UsersCommentsService usersCommentsService;

    // 创建评论
    @PostMapping("/add")
    public Result createComment(@RequestBody UsersComments comment) {
        boolean success = usersCommentsService.save(comment);
        return success ? Result.success(comment) : Result.error("创建评论失败");
    }

    // 获取所有评论
    @GetMapping("/getAll")
    public Result getAllComments() {
        List<UsersComments> comments = usersCommentsService.list();
        return Result.success(comments);
    }

    // 获取单个评论
    @GetMapping("/get/{id}")
    public Result getCommentById(@PathVariable("id") Integer id) {
        UsersComments comment = usersCommentsService.getById(id);
        return comment != null ? Result.success(comment) : Result.error("评论不存在");
    }

    // 更新评论
    @PostMapping("/update")
    public Result updateComment(@RequestBody UsersComments comment) {
        boolean success = usersCommentsService.updateById(comment);
        return success ? Result.success(comment) : Result.error("更新评论失败");
    }

    // 删除评论
    @PostMapping("/del/{id}")
    public Result deleteComment(@PathVariable("id") Integer id) {
        boolean success = usersCommentsService.removeById(id);
        return success ? Result.success() : Result.error("删除评论失败");
    }

    // 获取某个帖子的所有评论
    @GetMapping("/post/{postId}")
    public Result getCommentsByPost(@PathVariable("postId") Integer postId) {
        List<UsersComments> comments = usersCommentsService.lambdaQuery()
                .eq(UsersComments::getPostId, postId)
                .list();
        return Result.success(comments);
    }

    // 获取某个用户的所有评论
    @GetMapping("/userAll/{userId}")
    public Result getCommentsByUser(@PathVariable("userId") Integer userId) {
        List<UsersComments> comments = usersCommentsService.lambdaQuery()
                .eq(UsersComments::getUserId, userId)
                .list();
        return Result.success(comments);
    }
}
