package com.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.Result;
import com.coupon.pojo.Coupon;
import com.coupon.mapper.CouponMapper;
import com.coupon.pojo.CouponOrder;
import com.coupon.service.CouponOrderService;
import com.coupon.service.CouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Resource
    private CouponOrderService couponOrderService;
    /**
     * 验证优惠劵
     *
     * @param couponId 优惠劵id
     * @param userId   用户id
     */
    @Override
    public Boolean validateCoupon(int couponId, int userId) {
        // 获取优惠劵
        Coupon coupon = getById(couponId);
        // 判断优惠劵状态
        if (coupon.getStatus() == 3) {
            return false;
        }
        // 判断优惠劵当前时间是否可用
        if (coupon.getValidStartTime().isAfter(LocalDateTime.now())
                || coupon.getValidEndTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        // 验证通过
        return true;
    }

    /**
     * 获取抵扣
     *
     * @param orderAmount 订单金额
     * @param coupon      优惠劵
     */
    @Override
    public BigDecimal calculateDiscount(BigDecimal orderAmount, Coupon coupon) {
        if (coupon == null) {
            return BigDecimal.ZERO;
        }
        switch (coupon.getType()) {
            case 1: // 满减券
                return calculateFullReduction(orderAmount, coupon);
            case 2: // 折扣券
                return calculateDiscountRate(orderAmount, coupon);
            case 3: // 立减券
                return calculateDirectReduction(coupon);
            default:
                throw new IllegalArgumentException("不支持的优惠券类型");
        }
    }

    // 定义金额精度和舍入模式
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // 计算满减券
    private BigDecimal calculateFullReduction(BigDecimal orderAmount, Coupon coupon) {
        // 直接使用actual_value（单位：元）
        BigDecimal reductionAmount = new BigDecimal(coupon.getActualValue())
                .setScale(SCALE, ROUNDING_MODE);

        // 判断是否满足满减条件
        if (orderAmount.compareTo(reductionAmount) >= 0) {
            return reductionAmount;
        }
        return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
    }

    // 计算折扣券
    private BigDecimal calculateDiscountRate(BigDecimal orderAmount, Coupon coupon) {
        // 将actual_value转换为折扣比例（如85表示85%）
        BigDecimal discountRate = new BigDecimal(coupon.getActualValue())
                .divide(new BigDecimal(100), 4, ROUNDING_MODE);

        // 计算折扣金额，保留两位小数
        return orderAmount.multiply(BigDecimal.ONE.subtract(discountRate))
                .setScale(SCALE, ROUNDING_MODE);
    }

    // 计算立减券
    private BigDecimal calculateDirectReduction(Coupon coupon) {
        // 直接返回减免金额（单位：元）
        return new BigDecimal(coupon.getActualValue())
                .setScale(SCALE, ROUNDING_MODE);
    }

    /**
     * 根据id和storeId删除优惠劵
     *
     * @param id
     * @param storeId
     * @return
     */
    @Override
    public boolean removeByIdAndStoreId(int id, int storeId) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("store_id", storeId);
        return remove(queryWrapper);
    }

    /**
     * 根据用户id获取优惠劵
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    @Transactional
    public Page<Coupon> getCouponsByUser(int userId, int page, int size) {
        // 1. 创建分页对象
        Page<Coupon> pageInfo = new Page<>(page, size);

        // 2. 根据用户ID查询所有相关的优惠券订单
        QueryWrapper<CouponOrder> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("user_id", userId);
        List<CouponOrder> couponOrders = couponOrderService.list(orderQueryWrapper);

        // 3. 获取所有优惠券ID
        List<Integer> couponIds = couponOrders.stream()
                .map(CouponOrder::getCouponId)
                .collect(Collectors.toList());

        if (couponIds.isEmpty()) {
            return pageInfo;
        }

        // 4. 创建优惠券查询条件
        QueryWrapper<Coupon> couponQueryWrapper = new QueryWrapper<>();
        couponQueryWrapper.in("id", couponIds)
                .and(wrapper -> wrapper
                        .gt("valid_end_time", LocalDateTime.now()) // 未过期
                        .or()
                        .between("valid_end_time",
                                LocalDateTime.now().minusDays(7),
                                LocalDateTime.now()) // 过期7天内
                )
                .orderByAsc("valid_end_time"); // 按有效期升序排序

        // 5. 使用MyBatis-Plus分页查询
        return this.page(pageInfo, couponQueryWrapper);
    }

    /**
     * 优惠劵抢购
     */
    @Override
    @GlobalTransactional(name = "deductCoupon", rollbackFor = Exception.class)
    public Result deductCoupon(Integer couponId, Integer userId) {
        // 1. 校验优惠券是否存在且可用
        Coupon coupon = this.getById(couponId);
        if (coupon == null || coupon.getStatus() != 1) {
            return Result.error("优惠券不存在或已下架");
        }

        // 2. 校验库存
        if (coupon.getAvailableStock() <= 0) {
            return Result.error("优惠券已抢完");
        }

        // 3. 校验用户是否达到限购数量
        Long userOrderCount = couponOrderService.lambdaQuery()
                .eq(CouponOrder::getUserId, userId)
                .eq(CouponOrder::getCouponId, couponId)
                .count();
        if (userOrderCount >= coupon.getLimitPerUser()) {
            return Result.error("已达到限购数量");
        }

        // 4. 扣减库存（乐观锁）
        boolean updateSuccess = this.lambdaUpdate()
                .eq(Coupon::getId, couponId)
                .gt(Coupon::getAvailableStock, 0)
                .setSql("available_stock = available_stock - 1")
                .update();
        if (!updateSuccess) {
            return Result.error("库存不足");
        }

        // 5. 创建订单
        CouponOrder order = new CouponOrder()
                .setUserId(userId)
                .setCouponId(couponId)
                .setStatus((byte) 1); // 1:未支付
        couponOrderService.save(order);

        return Result.success("优惠券扣除成功");
    }


}
