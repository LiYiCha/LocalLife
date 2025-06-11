package com.coupon.controller;

import com.coupon.pojo.CouponOrder;
import com.coupon.service.CouponOrderService;
import com.core.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券订单表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */


@RestController
@RequestMapping("/api/coupon/order")
public class CouponOrderController {

    @Autowired
    private CouponOrderService couponOrderService;

    // 创建优惠券订单
    @PostMapping("/add")
    public Result createCouponOrder(@RequestBody CouponOrder couponOrder) {
        boolean success = couponOrderService.save(couponOrder);
        return success ? Result.success(couponOrder) : Result.error("创建订单失败");
    }

    // 获取所有订单
    @GetMapping("/getAll")
    public Result getAllOrders() {
        List<CouponOrder> orders = couponOrderService.list();
        return Result.success(orders);
    }

    // 获取单个订单
    @GetMapping("/getById")
    public Result getOrderById(@RequestParam("id") int id) {
        CouponOrder order = couponOrderService.getById(id);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    // 更新订单状态
    @PostMapping("/update")
    public Result updateOrder(@RequestBody CouponOrder order) {
        boolean success = couponOrderService.updateById(order);
        return success ? Result.success(order) : Result.error("更新订单失败");
    }

    // 删除订单
    @PostMapping("/delById")
    public Result deleteOrder(@RequestParam("id") int id) {
        boolean success = couponOrderService.removeById(id);
        return success ? Result.success() : Result.error("删除订单失败");
    }

    // 获取用户的订单
    @GetMapping("/userAll")
    public Result getOrdersByUser(@RequestParam("userId") int userId) {
        List<CouponOrder> orders = couponOrderService.lambdaQuery()
                .eq(CouponOrder::getUserId, userId)
                .list();
        return Result.success(orders);
    }

    // 获取某个优惠券的订单
    @GetMapping("/coupon")
    public Result getOrdersByCoupon(@RequestParam("couponId") int couponId) {
        List<CouponOrder> orders = couponOrderService.lambdaQuery()
                .eq(CouponOrder::getCouponId, couponId)
                .list();
        return Result.success(orders);
    }
    //使用优惠劵
    @PostMapping("/use")
    Result useCoupon(@RequestParam("userId") Integer userId, @RequestParam("couponId") Integer couponId){
        int success = couponOrderService.userCoupon(couponId, userId);
        return success > 0 ? Result.success() : Result.error("使用优惠劵失败");
    }
}
