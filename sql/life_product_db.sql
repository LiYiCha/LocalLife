-- ---------------------
-- /* 商品表 */
-- products
-- /* 商品分类表 */
-- product_categories
-- /* 商品评价表 */
-- product_reviews
-- /* 购物车表 */
-- shopping_cart
-- ------------------------------

/* 商品主表 */
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    merchant_id INT NOT NULL COMMENT '商家ID',
    category_id INT NOT NULL COMMENT '分类ID',
    product_name VARCHAR(255) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    status ENUM('AVAILABLE', 'OUT_OF_STOCK', 'DELETED') NOT NULL DEFAULT 'AVAILABLE' COMMENT '商品状态',
    sales_count INT DEFAULT 0 COMMENT '销量',
    is_recommend TINYINT(1) DEFAULT 0 COMMENT '是否推荐',
    sort_order INT DEFAULT 0 COMMENT '排序权重',
    main_image VARCHAR(255) COMMENT '商品主图',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant (merchant_id),
    INDEX idx_category (category_id),
    INDEX idx_status (status),
    INDEX idx_product_name(product_name)
) COMMENT='商品信息表';

/* 商品SKU表 */
CREATE TABLE product_skus (
    sku_id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'SKU ID',
    product_id INT NOT NULL COMMENT '商品ID',
    sku_name VARCHAR(255) NOT NULL COMMENT 'SKU名称(如：红色/L号,大碗小碗)',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    stock_quantity INT NOT NULL COMMENT '库存数量',
    image_url VARCHAR(255) COMMENT 'SKU图片',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product (product_id),
    CONSTRAINT fk_sku_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) COMMENT='商品SKU表';

/* 商品图片表 */
CREATE TABLE product_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    product_id INT NOT NULL COMMENT '商品ID',
    sku_id INT COMMENT '关联的SKU ID',  -- 新增字段
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    is_main TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否主图',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product (product_id),
    INDEX idx_sku (sku_id),  -- 新增索引
    CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    CONSTRAINT fk_image_sku FOREIGN KEY (sku_id) REFERENCES product_skus(sku_id) ON DELETE SET NULL  -- 新增外键
) COMMENT='商品图片表';

/* 商品分类表 */
CREATE TABLE product_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    merchant_id INT NOT NULL COMMENT '商家ID',  -- 新增字段，关联商家
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述',  -- 新增字段
    cover_image VARCHAR(255) COMMENT '分类封面图',  -- 新增字段
    level TINYINT NOT NULL COMMENT '分类层级：1-一级分类，2-二级分类',
    parent_id INT DEFAULT 0 COMMENT '父分类ID（一级分类为0）',
    is_show TINYINT DEFAULT 1 COMMENT '是否展示：0-隐藏，1-显示',  -- 新增字段
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant (merchant_id),  -- 新增索引
    INDEX idx_parent (parent_id)
) COMMENT='商品分类表';
-- 米粉店A的分类数据
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
(1, '特色米粉', '本店招牌米粉', 'https://example.com/mifen.jpg', 1, 0),
(1, '经典汤粉', '传统汤粉系列', 'https://example.com/tangfen.jpg', 2, 1),
(1, '创新拌粉', '新式拌粉系列', 'https://example.com/banfen.jpg', 2, 1),
(1, '特色小吃', '搭配米粉的小吃', 'https://example.com/xiaochi.jpg', 1, 0);

/* 商品评价表 */
CREATE TABLE product_reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
    product_id INT NOT NULL COMMENT '商品ID',
    order_id INT NOT NULL COMMENT '订单ID',
    user_id INT NOT NULL COMMENT '用户ID',
    rating TINYINT NOT NULL COMMENT '评分(1-5星)',
    content TEXT COMMENT '评价内容',
    reply TEXT COMMENT '商家回复',
    reply_time TIMESTAMP NULL COMMENT '回复时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product (product_id),
    INDEX idx_user (user_id),
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) COMMENT='商品评价表';

/* 购物车表 */
CREATE TABLE shopping_cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id INT NOT NULL COMMENT '用户ID',
    product_id INT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(255) NOT NULL COMMENT '商品名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    selected TINYINT(1) DEFAULT 1 COMMENT '是否选中',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_user_product (user_id, product_id),
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) COMMENT='购物车表';
