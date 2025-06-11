package com.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.pojo.UsersPosts;
import com.community.service.UsersPostsService;
import com.core.config.ReggieConfig;
import com.core.utils.FileUploadUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.core.utils.Result;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/community/posts")
public class UsersPostsController {

    @Autowired
    private UsersPostsService usersPostsService;
    @Autowired
    private ReggieConfig reggieConfig;

    /**
     * 创建帖子
     * @param post
     * @return
     */
    @PostMapping("/add")
    public Result createPost(@RequestBody UsersPosts post) {
        boolean success = usersPostsService.save(post);
        return success ? Result.success(post) : Result.error("创建帖子失败");
    }

    /**
     * 根据分类获取帖子
     * @param category
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/getAll")
    public Result getAllPosts(
            @RequestParam("category") String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<UsersPosts> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UsersPosts> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersPosts::getCategory,category)
                .orderByDesc(UsersPosts::getCreatedTime);

        Page<UsersPosts> result = usersPostsService.page(pageParam, wrapper);
        return Result.success(result);
    }

    /**
     * 根据id获取帖子
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public Result getPostById(@RequestParam("id") Integer id) {
        UsersPosts post = usersPostsService.getById(id);
        return post != null ? Result.success(post) : Result.error("帖子不存在");
    }

    /**
     * 更新帖子
     * @param post
     * @return
     */
    @PostMapping("/update")
    public Result updatePost(@RequestBody UsersPosts post) {
        boolean success = usersPostsService.updateById(post);
        return success ? Result.success(post) : Result.error("更新帖子失败");
    }
    /**
     * 点赞帖子
     * @param postId
     * @return
     */
    @PostMapping("/like")
    public Result likePost(@RequestParam("postId") Integer postId) {
        return usersPostsService.likePost(postId);
    }

    /**
     * 删除帖子
     * @param userId
     * @param id
     * @return
     */
    @PostMapping("/delById")
    public Result deletePost(@RequestParam("id") Integer id,
                             @RequestParam("userId") Integer userId) {
        QueryWrapper<UsersPosts> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id)
                .eq("user_id", userId);
        boolean success = usersPostsService.remove(wrapper);
        return success ? Result.success() : Result.error("删除帖子失败");
    }

    /**
     * 根据用户id获取帖子
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/userAll")
    public Result getPostsByUser(
            @RequestParam("userId") Integer userId,
            @RequestParam(defaultValue = "1",name="page") Integer page,
            @RequestParam(defaultValue = "10",name="size") Integer size) {

        Page<UsersPosts> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UsersPosts> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UsersPosts::getUserId, userId)
                .orderByDesc(UsersPosts::getCreatedTime);

        Page<UsersPosts> result = usersPostsService.page(pageParam, wrapper);
        return Result.success(result);
    }

    /**
     * 根据标题搜索帖子
     * @param title
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public Result searchPosts(@RequestParam("title") String title,
                              @RequestParam(defaultValue = "1",name="page") Integer page,
                              @RequestParam(defaultValue = "10",name="size") Integer size) {
            return usersPostsService.searchPosts(title,page,size);
    }

    /**
     * 上传媒体文件
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public Result uploadMedia(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = FileUploadUtil.uploadFile(file, reggieConfig.getPath());
            // 返回媒体访问 URL
            String mediaUrl = "/files/" + fileName;
            return Result.success(mediaUrl);
        } catch (RuntimeException e) {
            return Result.error("上传文件失败:"+e.getMessage());
        }
    }
    /**
     * 下载媒体文件
     * @param fileName
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName) {
        return FileUploadUtil.downloadFile(fileName, reggieConfig.getPath());
    }

    /**
     * 删除媒体文件
     * @param fileName
     * @return
     */
    @PostMapping("/delete")
    public Result deleteFile(@RequestParam("fileName") String fileName) {
        try {
            FileUploadUtil.deleteFile(fileName, reggieConfig.getPath());
            return Result.success("文件删除成功");
        } catch (RuntimeException e) {
            return Result.error("文件删除失败:"+e.getMessage());
        }
    }
}
