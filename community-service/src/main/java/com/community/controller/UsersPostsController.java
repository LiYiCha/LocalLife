package com.community.controller;

import com.community.pojo.UsersPosts;
import com.community.service.UsersPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.core.utils.Result;

import java.util.List;

/**
 * <p>
 * 交流社区帖子表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */


@RestController
@RequestMapping("/posts")
public class UsersPostsController {

    @Autowired
    private UsersPostsService usersPostsService;

    // 创建帖子
    @PostMapping("/add")
    public Result createPost(@RequestBody UsersPosts post) {
        boolean success = usersPostsService.save(post);
        return success ? Result.success(post) : Result.error("创建帖子失败");
    }

    // 获取所有帖子
    @GetMapping("getAll")
    public Result getAllPosts() {
        List<UsersPosts> posts = usersPostsService.list();
        return Result.success(posts);
    }

    // 获取单个帖子
    @GetMapping("/get/{id}")
    public Result getPostById(@PathVariable("id") Integer id) {
        UsersPosts post = usersPostsService.getById(id);
        return post != null ? Result.success(post) : Result.error("帖子不存在");
    }

    // 更新帖子
    @PostMapping("/update")
    public Result updatePost(@RequestBody UsersPosts post) {
        boolean success = usersPostsService.updateById(post);
        return success ? Result.success(post) : Result.error("更新帖子失败");
    }

    // 删除帖子
    @PostMapping("/del/{id}")
    public Result deletePost(@PathVariable Integer id) {
        boolean success = usersPostsService.removeById(id);
        return success ? Result.success() : Result.error("删除帖子失败");
    }

    // 获取用户的所有帖子
    @GetMapping("/userAll/{userId}")
    public Result getPostsByUser(@PathVariable Integer userId) {
        List<UsersPosts> posts = usersPostsService.lambdaQuery()
                .eq(UsersPosts::getUserId, userId)
                .list();
        return Result.success(posts);
    }
}
