package com.order.controller;

import com.order.dto.OrderDTO;
import com.order.enums.PaymentMethod;
import com.order.service.OrdersService;
import com.core.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 创建订单
     *
     * @param orderdto
     * @return
     */
    @PostMapping("/add")
    public Result createOrder(@RequestBody OrderDTO orderdto) {
        return ordersService.createOrder(orderdto);
    }

    @PostMapping("/addCart")
    public Result createOrderFromCart(@RequestParam("userId") Integer userId,
                                      @RequestParam("cartItemIds") List<Integer> cartItemIds,
                                      @RequestParam(required = false,name="couponId") Integer couponId) {
        return ordersService.createOrderFromCart(userId, cartItemIds, couponId);
    }

    /**
     * 支付订单
     *
     * @param orderId
     * @param paymentMethod
     * @return
     */
    @PostMapping("/pay")
    public Result payOrder(@RequestParam("orderId") Integer orderId,
                           @RequestParam("paymentMethod") String paymentMethod) {
        PaymentMethod method = PaymentMethod.fromCode(paymentMethod);
        return ordersService.payOrder(orderId, method);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public Result cancelOrder(@RequestParam("userId") Integer userId,
                              @RequestParam("orderId") Integer orderId) {
        return ordersService.cancelOrder(userId,orderId);
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    @PostMapping("/del")
    public Result delOrder(@RequestParam("userId") Integer userId,
                           @RequestParam("orderId") Integer orderId) {
        return ordersService.removeById(userId,orderId) ? Result.success() : Result.error("删除失败");
    }

    /**
     * 获取用户订单
     *
     * @param userId
     * @return
     */
    @GetMapping("/userAll")
    public Result getUserOrders(@RequestParam("userId") Integer userId,
                                @RequestParam(defaultValue = "1",name = "pageNum",required = false) Integer pageNum,
                                @RequestParam(defaultValue = "10",name = "pageSize",required = false) Integer pageSize) {
        return ordersService.getUserOrders(userId, pageNum, pageSize);
    }
}
