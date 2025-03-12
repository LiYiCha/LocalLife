-- ------------------------------
-- /* 优惠券表 */
-- coupons
-- /* 优惠券使用记录表 */
-- coupon_usage_log
-- ------------------------------

/* 优惠券表 */
CREATE TABLE coupon (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    store_id BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '商铺id',
    title VARCHAR(255) NOT NULL COMMENT '代金券标题',
    pay_value BIGINT UNSIGNED NOT NULL COMMENT '支付金额，单位是元',
    actual_value DECIMAL(15,2) NOT NULL COMMENT '抵扣金额，单位：元',
    type TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '1,满减券; 2,折扣券; 3,立减券',
    scope TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '1,全场通用; 2,指定分类; 3,指定商品',
    category_id BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '指定分类ID',
    product_id BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '指定商品ID',
    total_stock INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '总库存',
    available_stock INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '可用库存',
    limit_per_user INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '每人限购数量',
    valid_start_time DATETIME NOT NULL COMMENT '有效期开始时间',
    valid_end_time DATETIME NOT NULL COMMENT '有效期结束时间',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '1,上架; 2,下架; 3,过期',
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_store (store_id),
    INDEX idx_valid_time (valid_start_time, valid_end_time)
) COMMENT='优惠券表';

/* 优惠券订单表 */
CREATE TABLE coupon_order (
    id BIGINT NOT NULL COMMENT '主键',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '下单的用户id',
    coupon_id BIGINT UNSIGNED NOT NULL COMMENT '购买的代金券id',
    status TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '订单状态，1：未支付；2：已支付；3：已核销；4：已取消',
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    pay_time TIMESTAMP NULL DEFAULT NULL COMMENT '支付时间',
    use_time TIMESTAMP NULL DEFAULT NULL COMMENT '核销时间',
    PRIMARY KEY (id) USING BTREE,
    INDEX idx_user (user_id),
    INDEX idx_coupon (coupon_id)
) COMMENT='优惠券订单表';
