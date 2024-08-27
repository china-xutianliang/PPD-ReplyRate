-- 自动回复率记录表
CREATE TABLE IF NOT EXISTS response_rate_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 主键，自动递增
    simulator_type INT,           -- 模拟器类型 0雷电 1闪现 2机电 等等
    simulator_name VARCHAR(255),  -- 模拟器启动名称
    role_type INT,                     -- 角色类型 0商家 1顾客
    chat_name VARCHAR(255)         -- 聊天对象名称
);
