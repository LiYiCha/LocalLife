package com.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.utils.PageResult;
import com.core.utils.RedisUtil;
import com.feign.client.CouponClient;
import com.feign.client.ProductClient;
import com.feign.dto.ProductStockDTO;
import com.feign.pojo.ShoppingCart;
import com.order.config.SendOrder;
import com.order.dto.OrderDTO;
import com.order.dto.OrderItemDTO;
import com.order.enums.OrderStatus;
import com.order.enums.PaymentMethod;
import com.order.enums.PaymentStatus;
import com.order.mapper.*;
import com.order.pojo.OrderDetails;
import com.order.pojo.Orders;
import com.order.pojo.Payments;
import com.order.service.*;
import com.core.utils.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.vo.UserOrderVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrderDetailsService orderDetailsService;
    @Resource
    private PaymentsService paymentsService;
    @Resource
    private ProductClient productClient;
    @Resource
    private CouponClient couponClient;
    @Resource
    private OrdersMapper ordersManager;

    @Resource
    private SendOrder sendOrder;

    @Resource
    private RedisUtil redisUtil;

    private static final int LOCK_POOL_SIZE = 10;  // 分段锁锁槽位数量

    // 使用一致性哈希来减少冲突概率
    private String generateLockKey(String baseKey, Integer id) {
        return baseKey + "_" + (Math.abs(id.hashCode() % LOCK_POOL_SIZE));
    }

    /**
     * 创建订单
     *
     * @param orderDTO 订单信息
     * @return Result
     */
    @Override
    @GlobalTransactional(timeoutMills = 10000, name = "create-order-tx", rollbackFor = Exception.class)
    public Result createOrder(OrderDTO orderDTO) {
        // 使用分段锁
        String userId = String.valueOf(orderDTO.getUserId());
        String lockKey = generateLockKey("addOrder", orderDTO.getUserId());

        if (redisUtil.tryLock(lockKey, userId, 10)) {
            try {
                // 1. 参数校验
                if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
                    return Result.error("订单商品不能为空");
                }

                // 2. 库存校验与扣减（Feign调用product-service）
                List<ProductStockDTO> stockList = orderDTO.getItems().stream()
                        .map(item -> new ProductStockDTO(item.getProductId(), item.getQuantity()))
                        .collect(Collectors.toList());

                Result stockResult = productClient.deductStock(stockList);
                if (stockResult.getCode() != Result.success().getCode()) {
                    return Result.error("库存不足: " + stockResult.getMsg());
                }

                // 3. 计算金额
                BigDecimal totalAmount = calculateTotalAmount(orderDTO.getItems());
                BigDecimal discountAmount = BigDecimal.ZERO;

                // 4. 创建订单主表
                Orders order = new Orders();
                order.setUserId(orderDTO.getUserId());
                order.setOrderNo(generateOrderNo()); // 实现订单号生成逻辑
                order.setTotalAmount(totalAmount); // 暂时不扣除优惠券金额
                order.setStatus(OrderStatus.PENDING.getCode());
                order.setCreatedTime(LocalDateTime.now());
                baseMapper.insert(order);

                // 异步处理优惠券使用、订单详情保存、消息发送
                asyncProcessOrder(orderDTO, order.getOrderId(), totalAmount);

                return Result.success("订单ID:" + order.getOrderId() + "创建成功");
            } finally {
                redisUtil.releaseLock(lockKey, userId);
            }
        } else {
            return Result.error("请勿重复操作");
        }
    }

    // 异步处理方法
    @Async
    public void asyncProcessOrder(OrderDTO orderDTO, Integer orderId, BigDecimal totalAmount) {
        try {
            // 1. 使用优惠券（异步）
            if (orderDTO.getCouponId() != null) {
                Result couponResult = couponClient.useCoupon(orderDTO.getCouponId(), orderDTO.getUserId());
                if (couponResult.getCode() == Result.success().getCode()) {
                    BigDecimal discountAmount = calculateDiscount(orderDTO);
                    totalAmount = totalAmount.subtract(discountAmount);

                    // 更新订单金额
                    Orders order = new Orders();
                    order.setOrderId(orderId);
                    order.setTotalAmount(totalAmount);
                    baseMapper.updateById(order);
                }
            }

            // 2. 创建订单详情（异步）
            List<OrderDetails> details = orderDTO.getItems().stream()
                    .map(item -> {
                        OrderDetails detail = new OrderDetails();
                        detail.setOrderId(orderId);
                        detail.setProductName(item.getProductName());
                        detail.setProductId(item.getProductId());
                        detail.setQuantity(item.getQuantity());
                        detail.setUnitPrice(item.getPrice());
                        return detail;
                    }).collect(Collectors.toList());
            orderDetailsService.saveBatch(details);

            // 3. 发送消息到延迟队列（异步）
            sendOrder.sendOrderToDelayQueue(orderId);
        } catch (Exception e) {
            log.error("异步处理订单失败，订单ID: {}, 用户ID: {}, 错误信息: {}", orderId, orderDTO.getUserId(), e.getMessage(), e);
        }
    }


    // 辅助方法：计算优惠劵优惠金额
    private BigDecimal calculateDiscount(OrderDTO orderDTO) {
        // 3.1 验证优惠劵
        if (orderDTO.getCouponId() != null) {
            Result couponResult = couponClient.validateCoupon(orderDTO.getCouponId(), orderDTO.getUserId());
            if (couponResult.getCode() != Result.success().getCode()) {
                throw new RuntimeException("优惠券验证失败: " + couponResult.getMsg());
            }
        }
        // 3.2 计算优惠劵优惠金额
        if (orderDTO.getCouponId() != null) {
            Result couponResult = couponClient.getCouponDiscount(orderDTO.getCouponId());
            if (couponResult.getCode() != Result.success().getCode()) {
                throw new RuntimeException("计算优惠券金额失败: " + couponResult.getMsg());
            }
            return ((BigDecimal) couponResult.getData()).multiply(BigDecimal.valueOf(orderDTO.getItems().size()));
        } else {
            return BigDecimal.ZERO;
        }
    }

    // 辅助方法：计算总金额
    private BigDecimal calculateTotalAmount(List<OrderItemDTO> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).setScale(2, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // 辅助方法：生成订单号
    private String generateOrderNo() {
        return UUID.randomUUID().toString();
    }

    // 辅助方法：生成支付流水号
    private String generatePaymentNo() {
        return "P" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 从购物车创建订单
     * @param userId
     * @param cartItemIds
     * @param couponId
     * @return
     */
    @Override
    @GlobalTransactional(timeoutMills = 10000, name = "create-order-car-tx", rollbackFor = Exception.class)
    public Result createOrderFromCart(Integer userId, List<Integer> cartItemIds, Integer couponId) {
        // 使用分段锁
        String lockKey = generateLockKey("addOrderFromCart", userId);

        if (redisUtil.tryLock(lockKey, userId.toString(), 10)) {
            try {
                // 1. 获取购物车商品（优先从缓存中获取）
                String cacheKey = "cart_" + userId;
                List<ShoppingCart> cartItems = null;

                // 检查缓存是否存在
                String jsonData = (String) redisUtil.get(cacheKey);
                if (jsonData != null) {
                    // 缓存命中，解析数据
                    cartItems = JSON.parseObject(jsonData, new TypeReference<List<ShoppingCart>>() {});
                } else {
                    // 缓存未命中，从远程服务获取数据
                    Result cartResult = productClient.getCartByUser(userId);
                    if (cartResult.getCode() != Result.success().getCode()) {
                        return Result.error("获取购物车失败: " + cartResult.getMsg());
                    }
                    // 解析远程服务返回的数据
                    cartItems = JSON.parseObject(JSON.toJSONString(cartResult.getData()), new TypeReference<List<ShoppingCart>>() {});

                    // 将数据存入缓存，设置过期时间为30分钟
                    redisUtil.set(cacheKey, JSON.toJSONString(cartItems), 30, TimeUnit.MINUTES);
                }

                // 2. 过滤出指定的购物车商品
                List<ShoppingCart> selectedCartItems = cartItems.stream()
                        .filter(cartItem -> cartItemIds.contains(cartItem.getId()))
                        .collect(Collectors.toList());

                if (selectedCartItems.isEmpty()) {
                    return Result.error("购物车中没有选中的商品");
                }

                // 3. 构建 OrderDTO 对象
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setUserId(userId);
                orderDTO.setCouponId(couponId);
                orderDTO.setItems(selectedCartItems.stream()
                        .map(cartItem -> {
                            OrderItemDTO item = new OrderItemDTO();
                            item.setProductId(cartItem.getProductId());
                            item.setProductName(cartItem.getProductName());
                            item.setQuantity(cartItem.getQuantity());
                            item.setPrice(cartItem.getPrice());
                            return item;
                        })
                        .collect(Collectors.toList()));

                // 4. 调用 createOrder 方法创建订单
                Result order = createOrder(orderDTO);
                if (order.getCode() != Result.success().getCode()) {
                    // 如果订单创建失败，移除选中的购物车商品
                    productClient.deleteCartItems(cartItemIds);
                }
                return order;
            } finally {
                // 确保释放锁
                redisUtil.releaseLock(lockKey, userId.toString());
            }
        } else {
            return Result.error("请勿重复操作");
        }
    }



    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param userId  用户ID
     * @return Result
     */
    @Override
    @GlobalTransactional(timeoutMills = 30000, name = "cancel-order-tx", rollbackFor = Exception.class)
    public Result cancelOrder(Integer userId, Integer orderId) {
        // 使用分段锁
        String lockKey = generateLockKey("cancelOrder", orderId);

        if (redisUtil.tryLock(lockKey, orderId.toString(), 10)) {
            try {
                // 1. 获取订单
                Orders order = ordersManager.getOrdersByUserIdAndOrderId(userId, orderId);
                if (order == null) {
                    return Result.error("订单不存在");
                }
                // 2. 检查订单状态
                if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
                    return Result.error("订单状态异常，无法取消");
                }

                // 3. 恢复库存（Feign调用product-service）
                List<OrderDetails> orderDetails = orderDetailsService.lambdaQuery()
                        .eq(OrderDetails::getOrderId, orderId)
                        .list();

                List<ProductStockDTO> stockList = orderDetails.stream()
                        .map(detail -> new ProductStockDTO(detail.getProductId(), detail.getQuantity()))
                        .collect(Collectors.toList());

                Result stockResult = productClient.restoreStock(stockList);
                if (stockResult.getCode() != Result.success().getCode()) {
                    throw new RuntimeException("库存恢复失败: " + stockResult.getMsg());
                }

                // 4. 更新订单状态
                order.setStatus(OrderStatus.CANCELED.getCode());
                this.updateById(order);

                return Result.success("订单已取消");
            } finally {
                redisUtil.releaseLock(lockKey, orderId.toString());
            }
        } else {
            return Result.error("请勿重复操作");
        }
    }

    /**
     * 支付订单
     *
     * @param orderId       订单ID
     * @param paymentMethod 支付方式
     * @return Result
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "pay-order-tx", rollbackFor = Exception.class)
    public Result payOrder(Integer orderId, PaymentMethod paymentMethod) {
        // 获取分段锁键（基于订单ID哈希取模）
        String lockKey = generateLockKey("payOrder",orderId);

        if (redisUtil.tryLock(lockKey, orderId.toString(), 300)) {
            try {
                // 1. 获取订单
                Orders order = this.getById(orderId);
                if (order == null) {
                    return Result.error("订单不存在");
                }

                // 2. 检查订单状态
                if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
                    return Result.error("订单状态异常");
                }

                // 3. 调用支付接口
                // TODO: 这里需要对接具体的支付渠道（支付宝、微信等）
                boolean paymentSuccess = false;
                switch (paymentMethod) {
                    case ALIPAY:
                        // paymentSuccess = alipayService.pay(order);
                        // 测试直接成功
                        paymentSuccess = true;
                        break;
                    case WECHAT:
                        // paymentSuccess = wechatPayService.pay(order);
                        break;
                    default:
                        return Result.error("不支持的支付方式");
                }

                // 4. 更新订单状态
                if (paymentSuccess) {
                    order.setStatus(OrderStatus.PAID.getCode());
                    this.updateById(order);

                    // 创建支付记录
                    Payments payment = new Payments();
                    payment.setOrderId(orderId);
                    payment.setPaymentNo(generatePaymentNo()); // 假设有一个方法生成支付流水号
                    payment.setAmount(order.getTotalAmount());
                    payment.setStatus(PaymentStatus.PAID.getCode());
                    paymentsService.save(payment);
                    // 获取商品ID和销量
                    List<OrderDetails> orderDetails = orderDetailsService.lambdaQuery()
                            .eq(OrderDetails::getOrderId, orderId)
                            .list();

                    List<Map<String, Integer>> salesList = orderDetails.stream()
                            .map(detail -> {
                                Map<String, Integer> salesMap = new HashMap<>();
                                salesMap.put("productId", detail.getProductId());
                                salesMap.put("quantity", detail.getQuantity());
                                return salesMap;
                            })
                            .collect(Collectors.toList());

                    // 批量更新商品销量
                    productClient.addSalesBatch(salesList);

                    return Result.success(payment);
                }

                return Result.error("支付失败");
            } finally {
                redisUtil.releaseLock(lockKey, orderId.toString());
            }
        } else {
            return Result.error("请勿重复操作");
        }
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return Result
     */
    @Override
    public boolean removeById(Integer userId, Integer id) {
        // 使用分段锁
        String lockKey = generateLockKey("deleteOrder", id);

        if (redisUtil.tryLock(lockKey, id.toString(), 10)) {
            try {
                // 1. 根据用户id和订单id获取订单
                Orders order = ordersManager.getOrdersByUserIdAndOrderId(userId, id);
                if (order == null) {
                    return false;
                }

                // 2. 检查订单状态(已支付不可以删除)
                if (!OrderStatus.PAID.getCode().equals(order.getStatus())) {
                    return false;
                }

                // 3. 删除订单
                this.removeById(id);

                // 4. 删除订单详情
                orderDetailsService.lambdaUpdate()
                        .eq(OrderDetails::getOrderId, id)
                        .remove();

                return true;
            } finally {
                redisUtil.releaseLock(lockKey, id.toString());
            }
        } else {
            return false;
        }
    }

    /**
     * 获取用户订单
     *
     * @param userId 用户ID
     * @return Result
     */
    // 使用多表联查获取订单及其详情
    @Override
    public Result getUserOrders(Integer userId, Integer pageNum, Integer pageSize) {
        //使用缓存
        String cacheKey = "user_orders_" + userId + "_" + pageNum + "_" + pageSize;
        String jsonData = (String) redisUtil.get(cacheKey);

        if (jsonData != null) {
            PageResult<UserOrderVO> cachedResult = JSON.parseObject(jsonData, new TypeReference<PageResult<UserOrderVO>>() {});
            return Result.success(cachedResult);
        }

        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId)
                .orderByDesc(Orders::getCreatedTime);
        IPage<Orders> ordersPage = ordersManager.selectPage(page, queryWrapper);

        List<UserOrderVO> userOrderVOList = ordersPage.getRecords().stream()
                .map(order -> {
                    UserOrderVO userOrderVO = new UserOrderVO();
                    userOrderVO.setOrderId(order.getOrderId());
                    userOrderVO.setOrderNo(order.getOrderNo());
                    userOrderVO.setTotalAmount(order.getTotalAmount());
                    userOrderVO.setStatus(order.getStatus());
                    userOrderVO.setCreatedTime(order.getCreatedTime());

                    List<OrderDetails> details = orderDetailsService.lambdaQuery()
                            .eq(OrderDetails::getOrderId, order.getOrderId())
                            .list();
                    userOrderVO.setOrderDetails(details);

                    return userOrderVO;
                })
                .collect(Collectors.toList());

        PageResult<UserOrderVO> result = new PageResult<>(ordersPage.getTotal(), userOrderVOList);
        redisUtil.set(cacheKey, JSON.toJSONString(result), 30, TimeUnit.MINUTES);

        return Result.success(result);
    }


}
