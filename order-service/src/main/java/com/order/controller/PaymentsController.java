package com.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.utils.Result;
import com.order.pojo.Payments;
import com.order.service.PaymentsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 支付记录表 前端控制器
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@RestController
@RequestMapping("/api/order/payments")
public class PaymentsController {

    private final PaymentsService paymentService;
    public PaymentsController(PaymentsService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 支付结果
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "支付结果")
    @GetMapping("/result")
    public Result paymentResult(@RequestParam("orderId") Integer orderId){
        QueryWrapper<Payments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        Payments payments = paymentService.getOne(queryWrapper);
        return payments == null ? Result.error("支付结果不存在") : Result.success(payments);
    }
}
