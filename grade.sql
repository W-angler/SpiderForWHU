/*
Navicat MySQL Data Transfer

Source Server         : wangle
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : grade

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2015-09-28 11:02:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
  `owner_name` varchar(64) NOT NULL,
  `head` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `type` varchar(64) NOT NULL,
  `credit` varchar(64) NOT NULL,
  `teacher` varchar(64) NOT NULL,
  `academy` varchar(64) NOT NULL,
  `learnType` varchar(64) NOT NULL,
  `year` varchar(64) NOT NULL,
  `term` varchar(64) NOT NULL,
  `grade` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
