package com.coupon.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coupon.pojo.Coupon;
import com.coupon.service.CouponOrderService;
import com.coupon.service.CouponService;
import com.core.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 优惠券表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;
    @Resource
    private CouponOrderService cos;

    /**
     * 验证优惠券
     *
     * @param couponId 优惠券ID
     * @param userId   用户ID
     * @return 验证结果
     */
    @GetMapping("/validate")
    public Result validateCoupon(@Validated @RequestParam("couponId") Integer couponId,@Validated @RequestParam("userId") Integer userId) {
        //验证用户的优惠劵状态
        Boolean aBoolean = cos.validateCouponOrder(couponId, userId);
        if (!aBoolean) {
            return Result.error("优惠券不可用");
        }
        Boolean result = couponService.validateCoupon(couponId, userId);
        return result ? Result.success() : Result.error("优惠券验证失败");
    }

    /**
     * 获取优惠券折扣金额
     *
     * @param couponId    优惠券ID
     * @param orderAmount 订单金额（单位：元）
     * @return 折扣金额
     */
    @GetMapping("/discount")
    public Result getDiscount(
            @Validated @RequestParam("couponId") Integer couponId,
            @Validated @RequestParam("orderAmount") BigDecimal orderAmount) {

        // 1. 验证优惠券是否存在
        Coupon coupon = couponService.getById(couponId);
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }
        if (orderAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("订单金额必须大于0");
        }
        // 2. 计算折扣金额
        BigDecimal discount = couponService.calculateDiscount(orderAmount, coupon);
        return Result.success(discount);
    }

    /**
     * 抢购优惠券
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return 抢购结果
     */
    @PostMapping("/buy")
    public Result buyCoupon(@RequestParam("couponId") Integer couponId, @RequestParam("userId") Integer userId) {
        Result result = couponService.deductCoupon(couponId, userId);
        if (result.getCode()==Result.success().getCode()) {
            return Result.success("优惠劵ID"+couponId+"抢购成功");
        } else {
            return Result.error(result.getMsg());
        }
    }

    /**
     * 创建优惠券
     * @param coupon
     * @return
     */
    @PostMapping("/add")
    public Result createCoupon(@RequestBody Coupon coupon) {
        boolean success = couponService.save(coupon);
        return success ? Result.success(coupon) : Result.error("创建优惠券失败");
    }


    /**
     * 获取优惠券
     * @param couponId
     * @return
     */
    @GetMapping("/getById")
    public Result getCouponById(@RequestParam("couponId") Integer couponId) {
        Coupon coupon = couponService.getById(couponId);
        return coupon != null ? Result.success(coupon) : Result.error("优惠券不存在");
    }

    /**
     * 更新优惠券
     * @param coupon
     * @return
     */
    @PostMapping("/update")
    public Result updateCoupon(@RequestBody Coupon coupon) {
        boolean success = couponService.updateById(coupon);
        return success ? Result.success(coupon) : Result.error("更新优惠券失败");
    }

    /**
     * 商家删除优惠券
     * @param couponId
     * @param storeId
     * @return
     */
    @PostMapping("/delByIdStoreId")
    public Result deleteCoupon(@RequestParam("couponId") int couponId, @RequestParam("storeId") Integer storeId) {
        boolean success = couponService.removeByIdAndStoreId(couponId, storeId);
        return success ? Result.success() : Result.error("删除优惠券失败");
    }


    /**
     * 根据店铺ID获取优惠券
     * @param storeId
     * @return
     */
    @GetMapping("/storeAll")
    public Result getCouponsByStore(@RequestParam("storeId") Integer storeId) {
        List<Coupon> coupons = couponService.lambdaQuery()
                .eq(Coupon::getStoreId, storeId)
                .list();
        return Result.success(coupons);
    }

    /**
     * 根据用户ID获取优惠券
     * @param userId
     * @return
     */
    @GetMapping("/userAll")
    public Result getUserCoupons(
            @RequestParam("userId") Integer userId,
            @RequestParam(defaultValue = "1",name = "pageNum",required = false) Integer page,
            @RequestParam(defaultValue = "10",name = "size",required = false) Integer size) {
        return couponService.getCouponsByUser(userId, page, size);
    }
}
