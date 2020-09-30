/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : cloud

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-09-30 16:11:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `name` varchar(64) DEFAULT '' COMMENT '文件名',
  `url` varchar(64) DEFAULT NULL COMMENT '文件路劲',
  `size` varchar(64) DEFAULT '' COMMENT '文件大小',
  `author` varchar(64) DEFAULT '' COMMENT '作者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `share_flag` varchar(1) DEFAULT '0' COMMENT '是否分享(0:否，1:是)',
  `folder_id` int(11) DEFAULT NULL COMMENT '文件夹ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='文件表';

-- ----------------------------
-- Records of document
-- ----------------------------
INSERT INTO `document` VALUES ('9', '01，表格个人简历_一页简洁式.doc', 'E:\\cloud\\caiqun\\01，表格个人简历_一页简洁式.doc', '46KB', 'caiqun', '2020-09-28 09:56:02', '0', '17');
INSERT INTO `document` VALUES ('10', '03，简洁罗列式，简约.doc', 'E:\\cloud\\caiqun\\test\\03，简洁罗列式，简约.doc', '74KB', 'caiqun', '2020-09-28 10:00:12', '0', '1');
INSERT INTO `document` VALUES ('26', '文本文档.txt', 'E:\\cloud\\caiqun\\文本文档.txt', '0字节', 'caiqun', '2020-09-29 15:21:23', '0', '1');
INSERT INTO `document` VALUES ('27', '蓝色表格.doc', 'E:\\cloud\\caiqun\\2级-2\\蓝色表格.doc', '57KB', 'caiqun', '2020-09-30 12:44:02', '1', '57');
INSERT INTO `document` VALUES ('28', '文本文档.txt', 'E:\\cloud\\caiqun\\2级-2\\3级-1\\文本文档.txt', '0字节', 'caiqun', '2020-09-30 13:06:30', '0', '58');
INSERT INTO `document` VALUES ('31', '新建 Microsoft Word 文档.docx', 'E:\\cloud\\t1\\新建 Microsoft Word 文档.docx', '14KB', 't1', '2020-09-30 14:22:42', '0', '41');
INSERT INTO `document` VALUES ('32', 'JSBC-JS-SDK.md', 'E:\\cloud\\t1\\JSBC-JS-SDK.md', '19KB', 't1', '2020-09-30 14:23:40', '0', '41');
INSERT INTO `document` VALUES ('34', '12345.txt', 'E:\\cloud\\t3\\12345.txt', '81字节', 't3', '2020-09-30 14:25:02', '0', '78');
INSERT INTO `document` VALUES ('35', '人教版六年级第一单元测试卷（合）.pdf', 'E:\\cloud\\t3\\人教版六年级第一单元测试卷（合）.pdf', '453KB', 't3', '2020-09-30 14:25:32', '0', '78');
INSERT INTO `document` VALUES ('36', '2.jpg', 'E:\\cloud\\t3\\2.jpg', '45KB', 't3', '2020-09-30 14:25:49', '0', '78');
INSERT INTO `document` VALUES ('37', '1.jpg', 'E:\\cloud\\t3\\新建文件夹\\1.jpg', '75KB', 't3', '2020-09-30 14:26:17', '0', '79');
INSERT INTO `document` VALUES ('38', '2.jpg', 'E:\\cloud\\t3\\新建文件夹\\2.jpg', '45KB', 't3', '2020-09-30 14:26:25', '0', '79');

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) DEFAULT NULL COMMENT '文件夹名称',
  `path` varchar(255) DEFAULT NULL COMMENT '文件上一层路径',
  `author` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `parent_id` int(11) DEFAULT '0',
  `status` varchar(1) DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of folder
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `user_name` varchar(64) DEFAULT '' COMMENT '姓名',
  `password` varchar(64) DEFAULT '' COMMENT '密码',
  `iphone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `roles` varchar(1) DEFAULT '0' COMMENT '角色(0:普通用户，1:管理员)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` varchar(1) DEFAULT '1' COMMENT '状态(0:删除，1:正常)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '123', '', '', '1', '2020-09-22 09:33:42', '2020-09-22 09:33:51', '1');
