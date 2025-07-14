
-- 管理员表
CREATE TABLE admins (
                        admin_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
                        username VARCHAR(50) NOT NULL UNIQUE COMMENT '管理员账号',
                        password VARCHAR(255) NOT NULL COMMENT '密码哈希',
                        role VARCHAR(50) NOT NULL DEFAULT 'ADMIN' COMMENT '角色类型',
                        status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                        last_login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
                        created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='管理员表';


