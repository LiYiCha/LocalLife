package com.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.product.pojo.ShoppingCart;
import com.product.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 购物车表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 根据用户ID获取购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    @GetMapping("/user/{userId}")
    public Result getCartByUser(@PathVariable("userId") Integer userId) {
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("created_time");
        List<ShoppingCart> cartItems = shoppingCartService.list(queryWrapper);
        return Result.success(cartItems);
    }

    /**
     * 添加商品到购物车
     *
     * @param cartItem 购物车商品信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addToCart(@RequestBody ShoppingCart cartItem) {
        boolean success = shoppingCartService.save(cartItem);
        return success ? Result.success() : Result.error("添加购物车失败");
    }

    /**
     * 更新购物车商品数量
     *
     * @param cartItem 购物车商品信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result updateCartItem(@RequestBody ShoppingCart cartItem) {
        boolean success = shoppingCartService.updateById(cartItem);
        return success ? Result.success() : Result.error("更新购物车失败");
    }

    /**
     * 从购物车中删除商品
     *
     * @param id 购物车ID
     * @param userId 用户userId
     * @return 删除结果
     */
    @PostMapping("/del/{id}/{userId}")
    public Result deleteCartItem(@RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("user_id", userId);
        boolean success = shoppingCartService.remove(queryWrapper);
        return success ? Result.success() : Result.error("删除购物车失败");
    }
    /**
     * 从购物车中删除商品（支持批量删除）
     *
     * @param ids 购物车ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    public Result deleteCartItems(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的商品");
        }
        boolean success = shoppingCartService.removeBatchByIds(ids);
        return success ? Result.success() : Result.error("删除购物车失败");
    }
}
