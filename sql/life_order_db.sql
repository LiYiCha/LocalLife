-- ------------------------------
-- /* 订单表 */
-- orders
-- /* 订单明细表 */
-- order_details
-- /* 支付记录表 */
-- payments
-- ------------------------------


/* 订单表 */
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    user_id INT NOT NULL COMMENT '用户ID',
    merchant_id INT NOT NULL COMMENT '商家ID',
    order_no VARCHAR(255) NOT NULL COMMENT '订单编号',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '总金额',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '订单状态(待支付/已支付(待发货)/待收货/待评价/取消/退款退货/已完成)',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_order_no (order_no)
) COMMENT='订单信息表';

/* 订单详情表 */
CREATE TABLE order_details (
    detail_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单详情ID',
    order_id INT NOT NULL COMMENT '关联到 orders 表',
    product_id INT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(255) NOT NULL COMMENT '商品名称',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '单价',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='订单详情表';

/* 支付记录表 */
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '支付记录ID',
    order_id INT NOT NULL COMMENT '关联到 orders 表',
    payment_no VARCHAR(64) NOT NULL COMMENT '支付流水号',
    amount DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    status VARCHAR(10) NOT NULL  COMMENT '支付状态',
    payment_method VARCHAR(20) NOT NULL COMMENT '支付方式',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE INDEX uk_payment_no (payment_no)
) COMMENT='支付记录表';
