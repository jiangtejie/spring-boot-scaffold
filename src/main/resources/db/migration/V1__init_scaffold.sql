-- ===========================================================
-- Scaffold 初始化迁移脚本
-- 创建框架级表（业务表请追加 V2__xxx.sql）
-- ===========================================================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 示例：应用用户表（业务模块可根据需要修改）
CREATE TABLE IF NOT EXISTS app_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL COMMENT '用户名',
    nickname    VARCHAR(128) NULL     COMMENT '昵称',
    avatar_url  TEXT         NULL     COMMENT '头像 URL',
    created_at  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    updated_at  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    UNIQUE KEY uq_app_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用用户';

SET FOREIGN_KEY_CHECKS = 1;
