/*
Navicat MySQL Data Transfer

Source Server         : localhost_3307
Source Server Version : 50723
Source Host           : localhost:3307
Source Database       : springbootseckill

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2020-02-24 14:54:55
*/

SET FOREIGN_KEY_CHECKS=0;

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
-- Records of seckill
-- ----------------------------
INSERT INTO `seckill` VALUES ('1000', '一百块买华为', '1000', '100.00', '2020-02-21 00:00:00', '2020-01-01 00:00:00', '2021-01-01 00:00:00');
INSERT INTO `seckill` VALUES ('1001', '五十块钱买苹果', '1000', '50.00', '2020-02-21 00:00:00', '2020-01-01 00:00:00', '2021-01-01 00:00:00');
INSERT INTO `seckill` VALUES ('1002', '二十块钱买小米', '1000', '20.00', '2020-02-21 00:00:00', '2020-01-01 00:00:00', '2121-01-01 00:00:00');
INSERT INTO `seckill` VALUES ('1003', '10块钱买oppo', '1000', '10.00', '2020-01-01 00:00:00', '2020-02-01 00:00:00', '2020-02-28 00:00:00');
INSERT INTO `seckill` VALUES ('1004', '5块钱买三星', '1000', '5.00', '2019-01-01 00:00:00', '2020-01-01 00:00:00', '2020-02-01 00:00:00');

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

-- ----------------------------
-- Records of seckill_success
-- ----------------------------
