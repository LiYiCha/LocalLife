package com.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.order.dto.OrderDTO;
import com.order.enums.PaymentMethod;
import com.core.utils.Result;
import com.order.pojo.Orders;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.List;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
public interface OrdersService extends IService<Orders> {

        Result createOrder(OrderDTO orderdto);

        Result payOrder(Integer orderId, PaymentMethod paymentMethod);

        Result createOrderFromCart(Integer userId, List<Integer> cartItemIds, Integer couponId);

        Result cancelOrder(Integer userId,Integer orderId);

        boolean removeById(Integer userId,Integer id);

        Result getUserOrders(Integer userId,String status, Integer pageNum, Integer pageSize);

        Result getOrderDetail(Integer userId, Integer orderId);

        Result getOrdersByMerchant(Integer merchantId, String status, Integer pageNum, Integer pageSize);

        Result updateStatusByMerchant(Integer orderId, Integer merchantId);
}
