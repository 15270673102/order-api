-- 2019-10-18 start --

ALTER TABLE user_task
    ADD task_desc VARCHAR(255) NULL DEFAULT NULL COMMENT '任务描述';

DROP TABLE IF EXISTS `user_audit`;
CREATE TABLE `user_audit`
(
    `id`          int(11)                                                       NOT NULL AUTO_INCREMENT,
    `order_id`    int(11)                                                       NULL DEFAULT NULL COMMENT '订单外键',
    `content`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核内容',
    `create_time` datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- 2019-10-18 end --


-- 2019-10-22 start --

ALTER TABLE `user_order_crawl_comment`
    ADD INDEX `t_index` (`task_id`) USING BTREE;

ALTER TABLE `user_order_crawl_forward`
    ADD INDEX `t_index` (`task_id`) USING BTREE;

ALTER TABLE `user_order_crawl_like`
    ADD INDEX `t_index` (`task_id`) USING BTREE;

ALTER TABLE `user_order`
    ADD INDEX `u_index` (`user_id`) USING BTREE;
ALTER TABLE `user_order`
    ADD INDEX `t_index` (`task_id`) USING BTREE;

ALTER TABLE `cdkey`
    ADD INDEX `c_index` (`code_num`) USING BTREE;

ALTER TABLE `user_appeal`
    ADD INDEX `o_index` (`order_id`) USING BTREE;

ALTER TABLE `user_audit`
    ADD INDEX `o_index` (`order_id`) USING BTREE;

ALTER TABLE `user_task`
    ADD INDEX `u_index` (`user_id`) USING BTREE;


CREATE TABLE `sys_dept`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `dept_name`   varchar(255) DEFAULT NULL COMMENT '部门',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

insert into sys_dept(id, dept_name, create_time)
values (null, '市场中心', now());

ALTER TABLE `users`
    ADD COLUMN `dept_id`     int(0)       NULL COMMENT '部门外键',
    ADD COLUMN `active_code` varchar(255) NULL COMMENT '激活码';

update users
set dept_id     = 1,
    active_code = '6446e80a2e8241debcf9e1c96ada4ced'
where user_role = 2;

-- 2019-10-22 end --


-- 2019-10-31 start --

CREATE TABLE `user_order_check`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `order_id`     int(11)       DEFAULT NULL COMMENT '订单外键',
    `check_status` int(2)        DEFAULT NULL COMMENT '审核状态',
    `check_reason` varchar(1000) DEFAULT NULL COMMENT '审核原因',
    `create_time`  datetime      DEFAULT NULL COMMENT '审核时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4;


ALTER TABLE `user_task`
    ADD COLUMN `reserve_day` int(2) NULL COMMENT '保留时间(天)' AFTER `task_desc`;

update user_task
set reserve_day = 1

#脚本  1.py

-- 2019-10-31 end --


-- 2019-11-19 start--

ALTER TABLE `user_pay`
MODIFY COLUMN `pay_status` int(2) NULL DEFAULT NULL COMMENT '【0：未结算】【1：结算中】【2：已结算】【3.冻结】【4.异常】' AFTER `pay_time`;

#脚本  2.py

-- 2019-11-19 start--


-- 2019-11-27 start--

ALTER TABLE `users`
ADD COLUMN `wechat` varchar(255) NULL COMMENT '微信号' AFTER `real_name`;

-- 2019-11-27 end--