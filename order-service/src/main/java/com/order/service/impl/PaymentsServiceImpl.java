package com.order.service.impl;

import com.order.pojo.Payments;
import com.order.mapper.PaymentsMapper;
import com.order.service.PaymentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author 一茶
 * @since 2025-01-27
 */
@Service
public class PaymentsServiceImpl extends ServiceImpl<PaymentsMapper, Payments> implements PaymentsService {

}
