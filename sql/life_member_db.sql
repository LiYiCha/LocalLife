-- -------------------------
-- /* 用户信息表 */
-- users
-- /* 用户签到表 */
-- user_checkins
-- /* 用户积分明细表 */
-- user_points_log
-- /* 用户收藏表 */
-- user_favorites
-- /* 地址表 */
-- addresses
-- -------------------------

/* 用户信息表 */
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户账号',
    password VARCHAR(255) NOT NULL COMMENT '密码哈希',
    user_type VARCHAR(10) NOT NULL DEFAULT 'USER' COMMENT '用户类型：普通用户',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    last_login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    mobile VARCHAR(20) COMMENT '手机号码',
    nickname VARCHAR(64) COMMENT '名称',
    email VARCHAR(64) COMMENT '邮箱',
    header VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别1女2男0保密',
    birth DATE COMMENT '生日',
    sign VARCHAR(255) COMMENT '个性签名',
    total_points BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户累计积分',
    total_growth_value BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户累计成长值',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户信息表';

/* 用户签到表 */
CREATE TABLE user_checkins (
    checkin_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '签到记录ID',
    user_id INT NOT NULL COMMENT '关联到 users 表中的用户',
    checkin_date DATE NOT NULL COMMENT '签到日期',
    consecutive_days SMALLINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '连续签到天数',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_date (user_id, checkin_date)
) COMMENT='用户签到记录表';

/* 用户积分变动明细表 */
CREATE TABLE user_points_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动数量',
    type TINYINT NOT NULL COMMENT '积分类型：1-签到，2-消费，3-评价',
    source VARCHAR(50) COMMENT '积分来源描述',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='用户积分明细表';

/* 用户收藏表 */
CREATE TABLE user_favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '收藏类型：1-商品，2-店铺',
    target_id INT NOT NULL COMMENT '收藏对象ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_target (user_id, type, target_id)
) COMMENT='用户收藏表';

/* 地址表 */
CREATE TABLE addresses (
    address_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id BIGINT NOT NULL COMMENT '关联到 users 表中的用户',
    name VARCHAR(255) COMMENT '收货人姓名',
    phone VARCHAR(64) COMMENT '电话',
    province VARCHAR(100) COMMENT '省份/直辖市',
    city VARCHAR(100) COMMENT '城市',
    region VARCHAR(100) COMMENT '区',
    detail_address VARCHAR(255) COMMENT '详细地址(街道)',
    is_default tinyint DEFAULT 0 COMMENT '是否默认地址',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (address_id)
) COMMENT='地址信息表';
-- ------------------------------
-- 商家表
-- ------------------------------
-- ------------------------------
-- /* 商家表 */
-- merchants
-- /* 商家店铺表 */
-- merchant_stores
-- ------------------------------

/* 商家表 */
CREATE TABLE merchants (
    merchant_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '商家ID',
    username VARCHAR(50) NOT NULL COMMENT '商家用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码哈希',
    user_type VARCHAR(10) NOT NULL DEFAULT 'MERCHANT' COMMENT '用户类型：商家',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    last_login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    email VARCHAR(100) UNIQUE COMMENT '邮箱地址',
    mobile VARCHAR(20) COMMENT '手机号码',
    business_name VARCHAR(100) COMMENT '商家名称',
    business_category VARCHAR(50) COMMENT '商家所属行业类别',
    business_license VARCHAR(50) COMMENT '营业执照编号',
    avatar_url VARCHAR(255) COMMENT '商家头像URL',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='商家信息表';

/* 商家店铺表 */
CREATE TABLE merchant_stores (
    store_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '店铺ID',
    merchant_id INT NOT NULL COMMENT '关联到 merchants 表中的商家',
    store_name VARCHAR(100) NOT NULL COMMENT '店铺名称',
    description TEXT COMMENT '店铺描述',
    logo_url VARCHAR(255) COMMENT '店铺logoURL',
    address VARCHAR(255) COMMENT '店铺地址',
    day_of_week TINYINT NOT NULL COMMENT '星期几(1-7)',
    opening_time TIME NOT NULL COMMENT '开始营业时间',
    closing_time TIME NOT NULL COMMENT '结束营业时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-休息，1-营业',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='商家店铺信息表';

-- 密码123456
INSERT INTO merchants (username, password, email, mobile, business_name, business_category, business_license, avatar_url)
VALUES
    ('123@qq.com', '3AB810483B356B077C3AEB72CB0B70DB', 'merchant1@example.com', '1234567890', '餐厅A', '餐饮', 'BIZ123456', 'http://example.com/avatar1.jpg'),
    ('merchant2', '3AB810483B356B077C3AEB72CB0B70DB', 'merchant2@example.com', '0987654321', '美发沙龙B', '美容美发', 'BIZ789012', 'http://example.com/avatar2.jpg');
INSERT INTO merchant_stores (merchant_id, store_name, description, logo_url, address, day_of_week, opening_time, closing_time, status)
VALUES
    (1, '餐厅A旗舰店', '这是餐厅A的旗舰店铺，提供多种美食选择。', 'http://example.com/logo1.jpg', '北京市朝阳区某街道1号', 1, '09:00:00', '21:00:00', 1),
    (2, '美发沙龙B分店', '这是美发沙龙B的分店，专业发型设计与护理服务。', 'http://example.com/logo2.jpg', '上海市浦东新区某街道2号', 2, '10:00:00', '20:00:00', 0);
