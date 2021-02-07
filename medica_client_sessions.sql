/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.21 : Database - mesica_client_sessions
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE = ''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
/*Table structure for table `backup_list` */

DROP TABLE IF EXISTS `backup_list`;

CREATE TABLE `backup_list`
(
    `id`       int       NOT NULL AUTO_INCREMENT,
    `time_baq` timestamp NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4;

/*Table structure for table `sessionlabs` */

DROP TABLE IF EXISTS `sessionlabs`;

CREATE TABLE `sessionlabs`
(
    `id`        int NOT NULL AUTO_INCREMENT,
    `name`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `email`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `sessionId` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `testText`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `staffid`   int DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

/*Table structure for table `sessionpatients` */

DROP TABLE IF EXISTS `sessionpatients`;

CREATE TABLE `sessionpatients`
(
    `id`        int NOT NULL AUTO_INCREMENT,
    `name`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `email`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `sessionId` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `staffid`   int DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
