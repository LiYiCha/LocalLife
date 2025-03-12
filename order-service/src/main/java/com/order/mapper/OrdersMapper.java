package com.order.mapper;

import com.order.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    //根据用户id和订单id获取订单
    Orders getOrdersByUserIdAndOrderId(Integer userId, Integer orderId);
}
