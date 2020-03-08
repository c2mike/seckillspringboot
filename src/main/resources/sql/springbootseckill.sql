/*
Navicat MySQL Data Transfer

Source Server         : localhost_3307
Source Server Version : 50723
Source Host           : localhost:3307
Source Database       : springbootseckill

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2020-03-08 13:40:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for msg_log
-- ----------------------------
DROP TABLE IF EXISTS `msg_log`;
CREATE TABLE `msg_log` (
  `msg_id` int(11) NOT NULL,
  `context` varchar(255) DEFAULT NULL,
  `status` int(255) DEFAULT NULL,
  `try_count` int(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `next_try_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for seckill
-- ----------------------------
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) DEFAULT NULL,
  `item_quantity` int(255) DEFAULT NULL,
  `item_price` decimal(5,2) DEFAULT NULL,
  `creat_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`seckill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for seckill_success
-- ----------------------------
DROP TABLE IF EXISTS `seckill_success`;
CREATE TABLE `seckill_success` (
  `seckill_id` int(11) NOT NULL,
  `phone` bigint(255) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`phone`,`seckill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
