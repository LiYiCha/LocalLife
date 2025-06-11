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
    status  VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE' COMMENT '商品状态',
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
-- 商家A
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(1, 2, '经典牛肉汤粉', '精选上等牛肉，搭配浓郁汤底', 'AVAILABLE', 120, 1, 'https://example.com/beef-soup.jpg'),
(1, 2, '酸辣猪骨汤粉', '酸辣开胃，猪骨熬制汤底', 'AVAILABLE', 95, 0, 'https://example.com/sour-spicy.jpg');
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(1, 3, '麻辣拌粉', '麻辣鲜香，口感劲道', 'AVAILABLE', 80, 1, 'https://example.com/spicy-mix.jpg'),
(1, 3, '酱香拌粉', '秘制酱料，回味无穷', 'AVAILABLE', 70, 0, 'https://example.com/sauce-mix.jpg');
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(1, 4, '炸春卷', '外酥里嫩，经典小吃', 'AVAILABLE', 150, 1, 'https://example.com/spring-roll.jpg'),
(1, 4, '卤味拼盘', '多种卤味组合，下饭神器', 'AVAILABLE', 110, 0, 'https://example.com/luwei.jpg');
-- 商家B
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(1, 4, '炸春卷', '外酥里嫩，经典小吃', 'AVAILABLE', 150, 1, 'https://example.com/spring-roll.jpg'),
(1, 4, '卤味拼盘', '多种卤味组合，下饭神器', 'AVAILABLE', 110, 0, 'https://example.com/luwei.jpg');
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(2, 7, '有机西兰花', '无农药残留，健康之选', 'AVAILABLE', 130, 1, 'https://example.com/broccoli.jpg'),
(2, 7, '有机胡萝卜', '自然种植，富含维生素', 'AVAILABLE', 145, 0, 'https://example.com/carrot.jpg');
-- 商家C
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(3, 10, '深层清洁面部护理', '清除毛孔污垢，恢复肌肤光泽', 'AVAILABLE', 50, 1, 'https://example.com/facial-clean.jpg'),
(3, 10, '补水保湿面部护理', '深层补水，持久保湿', 'AVAILABLE', 45, 0, 'https://example.com/moisturize.jpg');
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(3, 11, '时尚短发造型', '个性化定制，展现独特魅力', 'AVAILABLE', 60, 1, 'https://example.com/short-hair.jpg'),
(3, 11, '优雅长发造型', '柔顺亮泽，尽显女性魅力', 'AVAILABLE', 55, 0, 'https://example.com/long-hair.jpg');
-- 商家D
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(4, 18, '五花肉', '肥瘦相间，适合红烧', 'AVAILABLE', 300, 1, 'https://example.com/pork-belly.jpg'),
(4, 18, '猪里脊肉', '肉质细嫩，适合炒菜', 'AVAILABLE', 280, 0, 'https://example.com/pork-tenderloin.jpg');
INSERT INTO products (merchant_id, category_id, product_name, description, status, sales_count, is_recommend, main_image) VALUES
(4, 20, '鲜活草鱼', '肉质鲜美，适合清蒸', 'AVAILABLE', 220, 1, 'https://example.com/grass-fish.jpg'),
(4, 20, '鲜活鲈鱼', '刺少肉嫩，营养丰富', 'AVAILABLE', 200, 0, 'https://example.com/seabass.jpg');


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
-- 商家A - 经典牛肉汤粉
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(1, '大碗', 28.00, 100, 'https://example.com/beef-soup-large.jpg'),
(1, '小碗', 22.00, 150, 'https://example.com/beef-soup-small.jpg');
-- 商家A - 酸辣猪骨汤粉
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(2, '大碗', 26.00, 120, 'https://example.com/sour-spicy-large.jpg'),
(2, '小碗', 20.00, 130, 'https://example.com/sour-spicy-small.jpg');
-- 商家A - 麻辣拌粉
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(3, '微辣', 24.00, 90, 'https://example.com/spicy-mix-mild.jpg'),
(3, '中辣', 24.00, 80, 'https://example.com/spicy-mix-medium.jpg'),
(3, '重辣', 24.00, 70, 'https://example.com/spicy-mix-hot.jpg');
-- 商家A - 酱香拌粉
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(4, '普通版', 22.00, 100, 'https://example.com/sauce-mix-regular.jpg'),
(4, '豪华版', 28.00, 80, 'https://example.com/sauce-mix-deluxe.jpg');
-- 商家A - 炸春卷
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(5, '单个装', 5.00, 200, 'https://example.com/spring-roll-single.jpg'),
(5, '家庭装（6个）', 28.00, 150, 'https://example.com/spring-roll-family.jpg');
-- 商家B - 有机西兰花
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(7, '500g', 12.00, 300, 'https://example.com/broccoli-500g.jpg'),
(7, '1kg', 22.00, 200, 'https://example.com/broccoli-1kg.jpg');
-- 商家C - 深层清洁面部护理
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(9, '基础套餐（1次）', 88.00, 50, 'https://example.com/facial-clean-basic.jpg'),
(9, '高级套餐（3次）', 240.00, 30, 'https://example.com/facial-clean-premium.jpg');
-- 商家D - 五花肉
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(13, '500g', 38.00, 150, 'https://example.com/pork-belly-500g.jpg'),
(13, '1kg', 72.00, 100, 'https://example.com/pork-belly-1kg.jpg');
-- 商家D - 鲜活草鱼
INSERT INTO product_skus (product_id, sku_name, price, stock_quantity, image_url) VALUES
(15, '1条（约500g）', 25.00, 80, 'https://example.com/grass-fish-500g.jpg'),
(15, '1条（约1kg）', 48.00, 60, 'https://example.com/grass-fish-1kg.jpg');



/* 商品图片表 */
CREATE TABLE product_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    product_id INT NOT NULL COMMENT '商品ID',
    sku_id INT COMMENT '关联的SKU ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序',
    media_type VARCHAR(255) DEFAULT 'IMAGE' COMMENT '媒体类型',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product (product_id),
    INDEX idx_sku (sku_id),
    CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    CONSTRAINT fk_image_sku FOREIGN KEY (sku_id) REFERENCES product_skus(sku_id) ON DELETE SET NULL  -- 新增外键
) COMMENT='商品图片表';

/* 商品分类表 */
CREATE TABLE product_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    merchant_id INT NOT NULL COMMENT '商家ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述',
    cover_image VARCHAR(255) COMMENT '分类封面图',
    level TINYINT NOT NULL COMMENT '分类层级：1-一级分类，2-二级分类',
    parent_id INT DEFAULT 0 COMMENT '父分类ID（一级分类为0）',
    is_show TINYINT DEFAULT 1 COMMENT '是否展示：0-隐藏，1-显示',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_merchant (merchant_id),
    INDEX idx_parent (parent_id),
    INDEX idx_name (name), -- 添加分类名称索引，支持模糊查询
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES product_categories(category_id) ON DELETE SET NULL
) COMMENT='商品分类表';
-- 商家A（餐饮类）
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
 (1, '特色米粉', '本店招牌米粉', 'https://example.com/mifen.jpg', 1, 0),
 (1, '经典汤粉', '传统汤粉系列', 'https://example.com/tangfen.jpg', 2, 1),
 (1, '创新拌粉', '新式拌粉系列', 'https://example.com/banfen.jpg', 2, 1),
 (1, '特色小吃', '搭配米粉的小吃', 'https://example.com/xiaochi.jpg', 1, 0);

-- 商家B（零售类）
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
(2, '生鲜果蔬', '新鲜蔬菜水果', 'https://example.com/fruit.jpg', 1, 0),
(2, '时令水果', '当季新鲜水果', 'https://example.com/seasonal-fruit.jpg', 2, 5),
(2, '有机蔬菜', '无农药绿色蔬菜', 'https://example.com/organic-veg.jpg', 2, 5),
(2, '日用百货', '家居日用品', 'https://example.com/daily-use.jpg', 1, 0);

-- 商家C（服务类）
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
(3, '美容美发', '专业美容美发服务', 'https://example.com/beauty.jpg', 1, 0),
(3, '面部护理', '深层清洁与保湿', 'https://example.com/facial-care.jpg', 2, 9),
(3, '发型设计', '个性化发型定制', 'https://example.com/hair-design.jpg', 2, 9),
(3, '健身瑜伽', '健康生活方式', 'https://example.com/yoga.jpg', 1, 0);

-- 商家D（生鲜类）
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
(4, '水果蔬菜', '新鲜水果和蔬菜', 'https://example.com/fruit-veg.jpg', 1, 0),
(4, '肉禽蛋品', '优质肉类、禽类和蛋品', 'https://example.com/meat-egg.jpg', 1, 0),
(4, '海鲜水产', '鲜活海鲜和水产品', 'https://example.com/seafood.jpg', 1, 0),
(4, '速食冷冻', '方便速食和冷冻食品', 'https://example.com/frozen-food.jpg', 1, 0),
(4, '粮油米面', '日常粮油和米面制品', 'https://example.com/rice-oil.jpg', 1, 0);

-- 商家D（生鲜类）二级分类
INSERT INTO product_categories (merchant_id, name, description, cover_image, level, parent_id) VALUES
(4, '时令水果', '当季新鲜水果', 'https://example.com/seasonal-fruit.jpg', 2, 13), -- 父分类ID为13（水果蔬菜）
(4, '有机蔬菜', '无农药绿色蔬菜', 'https://example.com/organic-veg.jpg', 2, 13), -- 父分类ID为13（水果蔬菜）
(4, '鲜猪肉', '优质新鲜猪肉', 'https://example.com/pork.jpg', 2, 14), -- 父分类ID为14（肉禽蛋品）
(4, '鸡蛋', '新鲜土鸡蛋', 'https://example.com/egg.jpg', 2, 14), -- 父分类ID为14（肉禽蛋品）
(4, '活鱼', '鲜活鱼类', 'https://example.com/live-fish.jpg', 2, 15), -- 父分类ID为15（海鲜水产）
(4, '虾蟹', '新鲜虾蟹类', 'https://example.com/shrimp-crab.jpg', 2, 15), -- 父分类ID为15（海鲜水产）
(4, '速冻水饺', '方便速冻水饺', 'https://example.com/frozen-dumplings.jpg', 2, 16), -- 父分类ID为16（速食冷冻）
(4, '冷冻肉类', '冷冻牛羊肉', 'https://example.com/frozen-meat.jpg', 2, 16), -- 父分类ID为16（速食冷冻）
(4, '大米', '优质大米', 'https://example.com/rice.jpg', 2, 17), -- 父分类ID为17（粮油米面）
(4, '食用油', '健康食用油', 'https://example.com/cooking-oil.jpg', 2, 17); -- 父分类ID为17（粮油米面）



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
    merchant_id INT NOT NULL COMMENT '商家ID',
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
