/*
Navicat MySQL Data Transfer

Source Server         : 201
Source Server Version : 50637
Source Host           : 192.168.88.201:3306
Source Database       : order_api

Target Server Type    : MYSQL
Target Server Version : 50637
File Encoding         : 65001

Date: 2019-09-27 11:00:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cdkey
-- ----------------------------
DROP TABLE IF EXISTS `cdkey`;
CREATE TABLE `cdkey` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `code_num` varchar(255) DEFAULT NULL COMMENT '激活码',
  `code_status` int(2) DEFAULT NULL COMMENT '【0：未激活】【1：已激活】',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cdkey
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) DEFAULT NULL COMMENT '登录名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `user_role` int(2) DEFAULT NULL COMMENT '用户角色 1.接单人 2.博主',
  `user_status` int(2) DEFAULT NULL COMMENT '【1：已注册】【2：已绑定】 【3：已冻结】',
  `credit_num` int(2) DEFAULT NULL COMMENT '信用次数',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `alipay` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of users
-- ----------------------------

-- ----------------------------
-- Table structure for user_appeal
-- ----------------------------
DROP TABLE IF EXISTS `user_appeal`;
CREATE TABLE `user_appeal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单外键',
  `role` int(11) DEFAULT NULL COMMENT '角色类型  1.user  2.admin',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `pics` varchar(2000) DEFAULT NULL COMMENT '图片集合',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_appeal
-- ----------------------------

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `user_id` int(11) DEFAULT NULL COMMENT '订单外键用户表id',
  `task_id` int(11) DEFAULT NULL COMMENT '订单外键任务id',
  `start_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `submit_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间',
  `order_num` int(11) DEFAULT NULL COMMENT '订单中的接单的任务数量',
  `order_price` double(11,2) DEFAULT NULL COMMENT '订单单价',
  `order_status` int(2) DEFAULT NULL COMMENT '【1：已接单】【2：已完成】【3：系统审核异常】\r\n【4.人工审核失败】【5：用户申诉中】',
  `failure_cause` varchar(1000) DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_order
-- ----------------------------

-- ----------------------------
-- Table structure for user_order_crawl_comment
-- ----------------------------
DROP TABLE IF EXISTS `user_order_crawl_comment`;
CREATE TABLE `user_order_crawl_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务外键id',
  `msg_id` varchar(255) DEFAULT NULL COMMENT '对应平台评论id',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `text` varchar(2000) DEFAULT NULL COMMENT '评论内容',
  `user_id` varchar(255) DEFAULT NULL COMMENT '对应平台用户uid',
  `screen_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_order_crawl_comment
-- ----------------------------

-- ----------------------------
-- Table structure for user_order_crawl_forward
-- ----------------------------
DROP TABLE IF EXISTS `user_order_crawl_forward`;
CREATE TABLE `user_order_crawl_forward` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务外键id',
  `msg_id` varchar(255) DEFAULT NULL COMMENT '对应平台评论id',
  `created_at` datetime DEFAULT NULL COMMENT '转发时间',
  `user_id` varchar(255) DEFAULT NULL COMMENT '对应平台用户uid',
  `screen_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_order_crawl_forward
-- ----------------------------

-- ----------------------------
-- Table structure for user_order_crawl_like
-- ----------------------------
DROP TABLE IF EXISTS `user_order_crawl_like`;
CREATE TABLE `user_order_crawl_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL COMMENT '任务外键id',
  `msg_id` varchar(255) DEFAULT NULL COMMENT '对应平台评论id',
  `created_at` datetime DEFAULT NULL COMMENT '点赞时间',
  `user_id` varchar(255) DEFAULT NULL COMMENT '对应平台用户uid',
  `screen_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_order_crawl_like
-- ----------------------------

-- ----------------------------
-- Table structure for user_pay
-- ----------------------------
DROP TABLE IF EXISTS `user_pay`;
CREATE TABLE `user_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_no` varchar(255) DEFAULT NULL COMMENT '流水账号',
  `order_id` int(11) DEFAULT NULL COMMENT '外键订单表id',
  `money` double(11,2) DEFAULT NULL COMMENT '结算金额',
  `pay_time` datetime DEFAULT NULL COMMENT '结算时间',
  `pay_status` int(2) DEFAULT NULL COMMENT '【0：未结算】【1：结算中】【2：已结算】',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_pay
-- ----------------------------

-- ----------------------------
-- Table structure for user_platform
-- ----------------------------
DROP TABLE IF EXISTS `user_platform`;
CREATE TABLE `user_platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '外键用户表id',
  `platform_type` int(2) DEFAULT NULL COMMENT '平台名称',
  `account_nick_name` varchar(255) DEFAULT NULL COMMENT '对应平台账号昵称',
  `account_uid` varchar(255) DEFAULT NULL COMMENT '对应平台账号id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_platform
-- ----------------------------

-- ----------------------------
-- Table structure for user_task
-- ----------------------------
DROP TABLE IF EXISTS `user_task`;
CREATE TABLE `user_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_no` varchar(255) DEFAULT NULL COMMENT '任务编号',
  `title` varchar(5000) DEFAULT NULL COMMENT '标题',
  `taskform` varchar(255) DEFAULT NULL COMMENT '任务类型【1：评论】【2：点赞】【3：转发】',
  `platform` int(3) DEFAULT NULL COMMENT '来源平台枚举',
  `source_link` varchar(255) DEFAULT NULL COMMENT '任务来源链接',
  `price` double(11,2) DEFAULT NULL COMMENT '任务单价',
  `total_num` int(11) DEFAULT NULL COMMENT '任务数量',
  `remain_num` int(11) DEFAULT NULL COMMENT '剩下任务数量',
  `task_status` int(2) DEFAULT NULL COMMENT '【1：发布中】2[已完成(接完单了)] 【3：已取消】 4[已结束(到时间了)]',
  `user_id` int(11) DEFAULT NULL COMMENT '任务发布人id',
  `consume_time` int(11) DEFAULT NULL COMMENT '任务耗时(分钟）',
  `publish_time` datetime DEFAULT NULL COMMENT '任务发布时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_task
-- ----------------------------
