package com.order.vo;

import com.order.pojo.OrderDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class UserOrderVO {
    private Integer orderId;
    private String orderNo;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdTime;
    private List<OrderDetails> orderDetails;

}
