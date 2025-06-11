package com.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.member.pojo.Addresses;
import com.member.service.AddressesService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 地址信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/api/member/addresses")
public class AddressesController {

    @Autowired
    private AddressesService ads;

    /**
     * 新增地址
     * @param address
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Addresses address){
        boolean flag = ads.addAddress(address);
        if (!flag){
            return Result.error("添加地址失败");
        }
        return Result.success("添加地址成功");
    }
    /**
     * 更新地址
     * @param address
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Addresses address){
        boolean flag = ads.updateAddress(address);
        if (!flag){
            return Result.error("更新地址失败");
        }
        return Result.success("更新地址成功");
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@Validated @RequestParam("id") Integer id){
        boolean flag = ads.removeById(id);
        if (!flag){
            return Result.error("删除地址失败");
        }
        return Result.success("删除地址成功");
    }

    /**
     * 批量删除地址
     * @param ids
     * @return
     */
    @PostMapping("deleteBatch")
    public Result deleteBatch(@RequestParam("ids") @Size(min = 1) Integer[] ids){
        boolean flag = ads.removeByIds(Arrays.asList(ids));
        if (!flag){
            return Result.error("批量删除地址失败！");
        }
        return Result.success("批量删除地址成功！");
    }

    /**
     * 获取全部地址
     * @param userId
     * @return
     */
    @GetMapping("/getAll")
    public Result get(@RequestParam("userId") Integer userId){
        QueryWrapper<Addresses> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        List<Addresses> address = ads.list(qw);
        return Result.success(address);
    }

    /**
     * 根据用户id获取默认地址
     * @param userId
     * @return
     */
    @GetMapping("/getDefaultByUserId")
    public Result getDefaultByUserId(@RequestParam("userId") Integer userId){
        QueryWrapper<Addresses> qw = new QueryWrapper<>();
        qw.eq("user_id",userId).eq("is_default",1);
        Addresses address = ads.getOne(qw);
        return Result.success(address);
    }
    /**
     * 根据地址id获取地址
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public Result getById(@RequestParam("id") Integer id){
        Addresses address = ads.getById(id);
        return Result.success(address);
    }
}
