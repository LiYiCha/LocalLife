-- ------------------------------
-- /* 社区帖子表 */
-- users_posts
-- /* 社区评论表 */
-- users_comments
-- ------------------------------

/* 社区交流表 */
CREATE TABLE users_posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    user_id INT NOT NULL COMMENT '关联到 users 表中的用户',
    title VARCHAR(255) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    category VARCHAR(50) NOT NULL COMMENT '帖子类别',
    liked INT DEFAULT 0 COMMENT '点赞数',
    comments INT DEFAULT 0 COMMENT '评论数',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='交流社区帖子表';

/* 评论表 */
CREATE TABLE users_comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id INT NOT NULL COMMENT '关联到 forum_posts 表',
    user_id INT NOT NULL COMMENT '关联到 users 表中的用户',
    content TEXT NOT NULL COMMENT '评论内容',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT='交流社区评论表';
