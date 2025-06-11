package com.order.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.order.dto.OrderDetailDTO;
import com.order.pojo.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;



/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    //根据用户id和订单id获取订单详细
    OrderDetailDTO getOrderDetail(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    //根据用户id和订单id获取订单
    Orders getOrder(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    //根据用户id，订单状态获取订单
    IPage<OrderDetailDTO> getUserOrders(Page<OrderDetailDTO> page,@Param("userId") Integer userId, @Param("status") String status);

    IPage<OrderDetailDTO> getOrdersByMerchant(@Param("merchantId") Integer merchantId, @Param("status") String status,@Param("page") Page<OrderDetailDTO> page);
}
