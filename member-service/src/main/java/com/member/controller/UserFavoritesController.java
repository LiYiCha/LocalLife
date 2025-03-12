package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.member.pojo.UserFavorites;
import com.member.service.UserFavoritesService;
import com.core.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 用户收藏表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/userFavorites")
public class UserFavoritesController {

    @Autowired
    private UserFavoritesService ufs;

    /**
     * 添加
     *
     * @param favorite
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody UserFavorites favorite) {
        boolean flag = ufs.save(favorite);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 修改
     *
     * @param favorite
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody UserFavorites favorite) {
        boolean flag = ufs.updateById(favorite);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam("id") Long id) {
        boolean flag = ufs.removeById(id);
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("deleteBatch")
    public Result deleteBatch(@RequestParam("ids") Long[] ids) {
        boolean flag = ufs.removeByIds(Arrays.asList(ids));
        if (!flag) {
            return Result.error();
        }
        return Result.success();
    }

    /**
     * 分页查询
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/get")
    public Result get(@Param("userId") Integer userId,
                      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                      @RequestParam(value = "size", required = false, defaultValue = "20") Integer size){
        Page<UserFavorites> pageRequest = new Page<>(page, size);
        QueryWrapper<UserFavorites> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        IPage<UserFavorites> pageResult = ufs.page(pageRequest, qw);
        return Result.success(pageResult);
    }
}
