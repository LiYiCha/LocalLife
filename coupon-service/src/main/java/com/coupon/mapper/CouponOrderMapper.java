package com.coupon.mapper;

import com.coupon.pojo.CouponOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * <p>
 * 优惠券订单表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface CouponOrderMapper extends BaseMapper<CouponOrder> {

    /**
     * 根据优惠券id和用户id查询优惠券订单
     *
     * @param couponId 优惠券id
     * @param userId   用户id
     * @return 优惠券订单
     */
    @Select("SELECT * FROM coupon_order WHERE coupon_id = #{couponId} AND user_id = #{userId}")
    CouponOrder selectByCouponIdAndUserId(int couponId,int userId);

    /**
     * 更新优惠券订单状态
     *
     * @param couponId 优惠券id
     * @param userId   用户id
     * @param status   状态
     * @return 更新结果
     */
    @Update("UPDATE coupon_order SET status = #{status} WHERE coupon_id = #{couponId} AND user_id = #{userId}")
    int updateStatus(int couponId,int userId,int status);

}
