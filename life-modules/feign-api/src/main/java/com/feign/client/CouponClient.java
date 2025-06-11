package com.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.core.utils.Result;

@FeignClient(name = "coupon-service", path = "/api/coupon")
public interface CouponClient {

    // 验证优惠券
    @GetMapping("/validate")
    Result validateCoupon(@RequestParam("couponId") int couponId, @RequestParam("userId") int userId);

    // 获取优惠券折扣
    @GetMapping("/discount")
    Result getCouponDiscount(@RequestParam("couponId") int couponId);
    //使用优惠券
    @GetMapping("/use")
    Result useCoupon(@RequestParam("couponId") int couponId, @RequestParam("userId") int userId);
}
