package com.coupon.service.impl;

import com.coupon.pojo.CouponOrder;
import com.coupon.mapper.CouponOrderMapper;
import com.coupon.service.CouponOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券订单表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class CouponOrderServiceImpl extends ServiceImpl<CouponOrderMapper, CouponOrder> implements CouponOrderService {

    @Resource
    private CouponOrderMapper com;

    /**
     * 验证优惠券是否可用
     * @param couponId
     * @param userId
     * @return
     */
    @Override
    public Boolean validateCouponOrder(int couponId, int userId) {
        CouponOrder couponOrder = com.selectByCouponIdAndUserId(couponId, userId);
        if(couponOrder==null||"".equals(couponOrder)){
            return false;
        }
        if(couponOrder.getStatus()!=2){
            return false;
        }
        return true;
    }
    @Override
    public int userCoupon(int couponId, int userId){
        //更新优惠劵为已使用
        return com.updateStatus(couponId,userId,3);
    }
}
