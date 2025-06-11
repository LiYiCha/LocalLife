package com.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.order.dto.OrderDTO;
import com.order.enums.PaymentMethod;
import com.order.pojo.Orders;
import com.order.pojo.Payments;
import com.order.service.OrdersService;
import com.core.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "订单控制器")
@RestController
@RequestMapping("/api/order/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 创建订单
     *
     * @param orderdto
     * @return
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/add")
    public Result createOrder(@RequestBody OrderDTO orderdto) {
        return ordersService.createOrder(orderdto);
    }

    /**
     * 购物车 创建订单
     *
     * @param userId
     * @param cartItemIds
     * @param couponId
     * @return
     */
    @ApiOperation(value = "购物车创建订单")
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
    @ApiOperation(value = "支付订单")
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
    @ApiOperation(value = "取消订单")
    @PostMapping("/cancel")
    public Result cancelOrder(@RequestParam("userId") Integer userId,
                              @RequestParam("orderId") Integer orderId) {
        return ordersService.cancelOrder(userId,orderId);
    }

    /**
     * 更新订单状态
     *
     * @param orders
     * @return
     */
    @ApiOperation(value = "更新订单状态")
    @PostMapping("/updateStatus")
    public Result updateStatus(Orders orders) {
        return ordersService.updateById(orders) ? Result.success() : Result.error("更新失败");
    }
    /**
     * 删除订单
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "删除订单")
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
    @ApiOperation(value = "获取用户订单")
    @GetMapping("/userAll")
    public Result getUserOrders(@RequestParam("userId") Integer userId,
                                @RequestParam(name="status",required = false) String status,
                                @RequestParam(defaultValue = "1",name = "pageNum",required = false) Integer pageNum,
                                @RequestParam(defaultValue = "10",name = "pageSize",required = false) Integer pageSize) {
        return ordersService.getUserOrders(userId,status, pageNum, pageSize);
    }


    /**
     * 根据订单id获取订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @ApiOperation(value = "根据订单id获取订单")
    @GetMapping("/getById")
    public Result getById(@RequestParam("userId") Integer userId,
                          @RequestParam("orderId") Integer orderId) {
        return ordersService.getOrderDetail(userId, orderId);
    }


    /**
     * 商家获取订单
     * @param merchantId
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商家获取订单")
    @GetMapping("/merchantAll")
    public Result getOrdersByMerchant(@RequestParam("merchantId") Integer merchantId,
                                    @RequestParam(name="status",required = false) String status,
                                    @RequestParam(defaultValue = "1",name = "pageNum",required = false) Integer pageNum,
                                    @RequestParam(defaultValue = "10",name = "pageSize",required = false) Integer pageSize) {
        return ordersService.getOrdersByMerchant(merchantId,status, pageNum, pageSize);
    }

    /**
     * 商家更新订单状态(发货)
     * @param orderId
     * @param merchantId
     * @return
     */
    @ApiOperation(value = "商家更新订单状态")
    @PostMapping("/updateByMerchant")
    public Result updateStatusByMerchant(@RequestParam("orderId") Integer orderId,
                                        @RequestParam("merchantId") Integer merchantId) {
        return ordersService.updateStatusByMerchant(orderId, merchantId);
    }
}
