package com.order.service.impl;

import com.order.pojo.OrderDetails;
import com.order.mapper.OrderDetailsMapper;
import com.order.service.OrderDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {

}
