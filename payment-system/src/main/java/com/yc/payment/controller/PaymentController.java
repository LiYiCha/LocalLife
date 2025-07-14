package com.yc.payment.controller;//package com.yc.payment.controller;
//
//import com.yc.payment.dto.PaymentRequest;
//import com.yc.payment.dto.RefundRequest;
//import com.yc.payment.service.AlipayService;
//import com.yc.payment.utils.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 支付宝支付接口
// */
//@RestController
//@RequestMapping("/api/payment/order")
//public class PaymentController {
//
//    private final AlipayService alipayService;
//
//    //private final WechatPaymentService wechatPayService;
//
//    public PaymentController(AlipayService alipayService) {
//        this.alipayService = alipayService;
//        //this.wechatPayService = wechatPayService;
//    }
//
//    /**
//     * 创建支付宝支付订单
//     * pr:订单号、商品/服务名称、金额、商品/服务详情
//     * @return 支付宝支付订单信息
//     */
//    @PostMapping("/alipay/create")
//    public Result createAlipayOrder(@RequestBody PaymentRequest pr){
//        return alipayService.createOrder(pr);
//    }
//
//    /**
//     * 创建支付宝退款订单
//     *
//     * @param rr 退款请求参数
//     * @return 支付宝退款订单信息
//     * 参数:orderId(必须)、refundAmount(必须)、refundReason(可选)、operatorId(操作员ID)、outRequestNo(退款请求号)
//     */
//    @PostMapping("/alipay/refund")
//    public Result refundAliPayOrder(@RequestBody RefundRequest rr){
//        return alipayService.refund(rr);
//    }
//
//     /**
//     * 查询支付宝支付订单
//     *
//     * @param orderId 订单号
//     * @return 支付宝支付订单信息
//     */
//     @GetMapping("/alipay/query")
//     public Result queryAlipayOrder(@RequestParam String orderId){
//         return alipayService.queryOrder(orderId);
//     }
//
//
//
//
////    /**
////     * 创建微信支付订单
////     *  pr:订单号、商品/服务名称、金额、商品/服务详情
////     * @return 微信支付订单信息
////     */
////    @PostMapping("/wechatpay/create")
////    public String createWechatPayOrder(@RequestBody PaymentRequest pr) {
////        return wechatPayService.createJsapiOrder(pr);
////    }
////
////    /**
////     * 查询微信支付订单
////     *
////     * @param outTradeNo 商户订单号
////     * @return 微信支付订单信息
////     */
////    @GetMapping("/wechatpay/query")
////    public Object queryWechatPayOrder(@RequestParam String outTradeNo) {
////        return wechatPayService.queryOrder(outTradeNo);
////    }
//
//}
