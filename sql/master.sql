CREATE DATABASE IF NOT EXISTS `db_qqspider_master`;
 GRANT ALL PRIVILEGES ON `db_qqspider_master`.* TO 'spideradmin'@'%' IDENTIFIED BY '2016spider';
USE `db_qqspider_master`;

CREATE TABLE IF NOT EXISTS `t_queue_status`(
	`tablename` VARCHAR(32) PRIMARY KEY,
	`idstart` BIGINT,
	`idend` BIGINT
);

CREATE TABLE IF NOT EXISTS `t_schedule_user_key` (
	`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
	`qq` BIGINT NOT NULL UNIQUE KEY -- 为了去重，必须有unique key
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_schedule_follow_key` (-- 先不做多级队列，这样不用写Master
	`id` BIGINT PRIMARY KEY AUTO_INCREMENT,
	`qq` BIGINT NOT NULL,
	`pageNum` INT NOT NULL,
	UNIQUE KEY `t_schedule_follow_key_qq_pagenum`(`qq`,`pageNum`) -- 为了去重，必须有unique key
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;
