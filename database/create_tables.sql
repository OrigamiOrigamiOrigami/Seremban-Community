-- 医院管理系统数据库表创建脚本
-- 创建时间: 2025-09-01
-- 作者: Origami

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hospital DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hospital;

-- 1. 科室表
DROP TABLE IF EXISTS fr_department;
CREATE TABLE fr_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(60) NOT NULL COMMENT '科室名称',
    type INT(1) NOT NULL DEFAULT 1 COMMENT '科室类型：1-门诊科室，2-住院科室，3-医技科室，4-行政科室',
    description VARCHAR(500) COMMENT '科室描述',
    recommended INT(1) NOT NULL DEFAULT 0 COMMENT '推荐级别：0-不推荐，1-推荐',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    deleted INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    tenant_id BIGINT COMMENT '租户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='科室表';

-- 2. 诊室表
DROP TABLE IF EXISTS fr_consultingroom;
CREATE TABLE fr_consultingroom (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '诊室id',
    name VARCHAR(60) NOT NULL COMMENT '诊室名称',
    description VARCHAR(500) COMMENT '诊室描述',
    department_id BIGINT NOT NULL COMMENT '隶属科室',
    status INT(1) NOT NULL DEFAULT 1 COMMENT '诊室状态：0-禁用，1-启用',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    deleted INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    tenant_id BIGINT COMMENT '租户ID',
    
    -- 外键约束
    CONSTRAINT fk_consultingroom_department FOREIGN KEY (department_id) REFERENCES fr_department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='诊室表';

-- 3. 管理员表
DROP TABLE IF EXISTS fr_admin;
CREATE TABLE fr_admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(60) NOT NULL COMMENT '姓名',
    username VARCHAR(60) NOT NULL UNIQUE COMMENT '用户名',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(60) COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    salt VARCHAR(255) COMMENT '盐',
    image VARCHAR(255) COMMENT '头像地址',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    deleted INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    tenant_id BIGINT COMMENT '租户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 4. 角色表
DROP TABLE IF EXISTS fr_role;
CREATE TABLE fr_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    role_name VARCHAR(60) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    deleted INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    tenant_id BIGINT COMMENT '租户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 5. 权限表
DROP TABLE IF EXISTS fr_permission;
CREATE TABLE fr_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    permission_name VARCHAR(60) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(255) NOT NULL COMMENT '权限标识',
    description VARCHAR(255) COMMENT '权限描述',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    deleted INT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    tenant_id BIGINT COMMENT '租户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 6. 管理员角色中间表
DROP TABLE IF EXISTS fr_admin_role;
CREATE TABLE fr_admin_role (
    admin_id BIGINT NOT NULL COMMENT '管理员ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    tenant_id BIGINT COMMENT '租户ID',

    PRIMARY KEY (admin_id, role_id),
    CONSTRAINT fk_admin_role_admin FOREIGN KEY (admin_id) REFERENCES fr_admin(id) ON DELETE CASCADE,
    CONSTRAINT fk_admin_role_role FOREIGN KEY (role_id) REFERENCES fr_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员角色中间表';

-- 7. 角色权限中间表
DROP TABLE IF EXISTS fr_role_permission;
CREATE TABLE fr_role_permission (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    tenant_id BIGINT COMMENT '租户ID',

    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES fr_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES fr_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限中间表';

-- 创建索引
-- 科室表索引
CREATE INDEX idx_department_name ON fr_department(name);
CREATE INDEX idx_department_type ON fr_department(type);
CREATE INDEX idx_department_deleted ON fr_department(deleted);

-- 诊室表索引
CREATE INDEX idx_consultingroom_name ON fr_consultingroom(name);
CREATE INDEX idx_consultingroom_department_id ON fr_consultingroom(department_id);
CREATE INDEX idx_consultingroom_status ON fr_consultingroom(status);
CREATE INDEX idx_consultingroom_deleted ON fr_consultingroom(deleted);

-- 管理员表索引
CREATE INDEX idx_admin_username ON fr_admin(username);
CREATE INDEX idx_admin_phone ON fr_admin(phone);
CREATE INDEX idx_admin_deleted ON fr_admin(deleted);

-- 角色表索引
CREATE INDEX idx_role_name ON fr_role(role_name);
CREATE INDEX idx_role_deleted ON fr_role(deleted);

-- 权限表索引
CREATE INDEX idx_permission_code ON fr_permission(permission_code);
CREATE INDEX idx_permission_name ON fr_permission(permission_name);
CREATE INDEX idx_permission_deleted ON fr_permission(deleted);

-- 管理员角色中间表索引
CREATE INDEX idx_admin_role_admin_id ON fr_admin_role(admin_id);
CREATE INDEX idx_admin_role_role_id ON fr_admin_role(role_id);

-- 角色权限中间表索引
CREATE INDEX idx_role_permission_role_id ON fr_role_permission(role_id);
CREATE INDEX idx_role_permission_permission_id ON fr_role_permission(permission_id);

-- 插入测试数据
-- 科室测试数据
INSERT INTO fr_department (name, type, description, recommended) VALUES
('内科', 1, '内科门诊，主要诊治内科疾病', 1),
('外科', 1, '外科门诊，主要进行外科手术', 1),
('儿科', 1, '儿科门诊，专门诊治儿童疾病', 1),
('妇产科', 2, '妇产科住院部，提供妇产科住院服务', 1),
('骨科', 2, '骨科住院部，专门治疗骨科疾病', 0),
('放射科', 3, '医技科室，提供影像检查服务', 0),
('检验科', 3, '医技科室，提供各种检验服务', 0),
('行政办公室', 4, '医院行政管理部门', 0);

-- 诊室测试数据
INSERT INTO fr_consultingroom (name, description, department_id, status) VALUES
('内科1诊室', '内科第一诊室', 1, 1),
('内科2诊室', '内科第二诊室', 1, 1),
('外科1诊室', '外科第一诊室', 2, 1),
('外科2诊室', '外科第二诊室', 2, 1),
('儿科1诊室', '儿科第一诊室', 3, 1),
('儿科2诊室', '儿科第二诊室', 3, 0),
('妇产科1诊室', '妇产科第一诊室', 4, 1),
('骨科1诊室', '骨科第一诊室', 5, 1);