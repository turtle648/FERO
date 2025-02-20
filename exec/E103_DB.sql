CREATE DATABASE  IF NOT EXISTS `e103_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `e103_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: e103_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `best_records`
--

DROP TABLE IF EXISTS `best_records`;
/*!50001 DROP VIEW IF EXISTS `best_records`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `best_records` AS SELECT 
 1 AS `user_id`,
 1 AS `exercise_type`,
 1 AS `best_count_1min`,
 1 AS `best_count_2min`,
 1 AS `best_count_5min`,
 1 AS `best_count_10min`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `sender_id` varchar(30) NOT NULL,
  `message` text NOT NULL,
  `sent_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `sender_id` (`sender_id`),
  CONSTRAINT `chat_message_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `chat_room` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chat_message_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_room`
--

DROP TABLE IF EXISTS `chat_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id_1` varchar(30) NOT NULL,
  `user_id_2` varchar(30) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_1` (`user_id_1`,`user_id_2`),
  KEY `user_id_2` (`user_id_2`),
  CONSTRAINT `chat_room_ibfk_1` FOREIGN KEY (`user_id_1`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `chat_room_ibfk_2` FOREIGN KEY (`user_id_2`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_room`
--

LOCK TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercise_log`
--

DROP TABLE IF EXISTS `exercise_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `exercise_duration` int unsigned NOT NULL,
  `exercise_cnt` int unsigned NOT NULL,
  `exercise_stats_ratio_id` bigint NOT NULL,
  `exercise_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `exercise_stats_ratio_id` (`exercise_stats_ratio_id`),
  CONSTRAINT `exercise_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_character` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `exercise_log_ibfk_2` FOREIGN KEY (`exercise_stats_ratio_id`) REFERENCES `exercise_stats_ratio` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise_log`
--

LOCK TABLES `exercise_log` WRITE;
/*!40000 ALTER TABLE `exercise_log` DISABLE KEYS */;
INSERT INTO `exercise_log` VALUES (1,'han777',67,2,2,'2024-09-19 06:59:05'),(2,'kwon567',14,4,2,'2024-12-12 06:59:05'),(3,'park234',116,3,2,'2024-08-12 06:59:05'),(4,'yang666',102,1,2,'2024-10-23 06:59:05'),(5,'shin444',23,2,2,'2024-02-29 06:59:05'),(6,'bae000',66,3,2,'2024-11-04 06:59:05'),(7,'seo999',82,1,2,'2025-01-10 06:59:05'),(8,'yoon333',11,4,2,'2024-12-22 06:59:05'),(9,'kim456',97,2,2,'2025-01-18 06:59:05'),(10,'lee789',20,4,2,'2024-10-05 06:59:05'),(11,'cha123',121,4,2,'2024-04-23 06:59:05'),(12,'song555',167,4,2,'2024-05-11 06:59:05'),(13,'choi567',69,1,2,'2024-03-07 06:59:05'),(14,'oh888',15,1,2,'2024-05-14 06:59:05'),(15,'ryu456',124,1,2,'2024-08-30 06:59:05'),(16,'jung111',18,4,2,'2024-10-30 06:59:05'),(17,'ssafy123',126,4,2,'2024-12-12 06:59:05'),(18,'joo345',167,1,2,'2024-10-09 06:59:05'),(19,'moon234',161,4,2,'2024-09-01 06:59:05'),(20,'kang222',91,4,2,'2024-10-22 06:59:05'),(32,'ssafy123',60,15,2,'2025-01-02 23:30:00'),(33,'ssafy123',120,25,2,'2025-01-05 00:15:00'),(34,'ssafy123',180,35,2,'2025-01-05 09:45:00'),(35,'ssafy123',60,18,2,'2025-01-07 22:30:00'),(36,'ssafy123',90,20,2,'2025-01-10 10:00:00'),(37,'ssafy123',60,17,2,'2025-01-12 01:30:00'),(38,'ssafy123',120,30,2,'2025-01-12 07:45:00'),(39,'ssafy123',60,15,2,'2025-01-14 23:00:00'),(40,'ssafy123',180,40,2,'2025-01-17 08:30:00'),(41,'ssafy123',90,22,2,'2025-01-19 21:45:00'),(42,'ssafy123',120,28,2,'2025-01-22 09:15:00'),(43,'ssafy123',60,16,2,'2025-01-24 22:30:00'),(44,'ssafy123',120,30,2,'2025-01-27 07:00:00'),(45,'ssafy123',180,45,2,'2025-01-30 09:45:00'),(46,'ssafy123',60,18,2,'2025-02-01 00:00:00'),(47,'ssafy123',90,22,2,'2025-02-02 08:30:00'),(48,'ssafy123',120,32,2,'2025-02-03 22:45:00'),(49,'ssafy123',60,16,2,'2025-02-05 09:30:00'),(50,'ssafy123',180,40,2,'2025-02-08 01:15:00'),(51,'ssafy123',90,25,2,'2025-02-09 21:45:00'),(52,'ssafy123',120,35,2,'2025-02-10 10:00:00'),(53,'ssafy123',60,19,2,'2025-02-11 23:15:00'),(54,'ssafy123',180,45,2,'2025-02-15 07:30:00'),(55,'ssafy123',90,24,2,'2025-02-16 22:00:00'),(56,'ssafy123',120,30,2,'2025-02-18 10:45:00');
/*!40000 ALTER TABLE `exercise_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `after_exercise_log_insert` AFTER INSERT ON `exercise_log` FOR EACH ROW BEGIN
    DECLARE exp_gain INT;
    DECLARE chest_ratio, back_ratio, stamina_ratio, arms_ratio, legs_ratio, abs_ratio DECIMAL(5,2);
    DECLARE user_char_id BIGINT;
    DECLARE stat_exists INT;

    -- exercise_stats_ratio 존재 여부 확인
    SELECT COUNT(*) INTO stat_exists
    FROM exercise_stats_ratio
    WHERE id = NEW.exercise_stats_ratio_id;

    IF stat_exists > 0 THEN
        -- 운동 비율 가져오기
    SELECT
        COALESCE(chest_ratio, 0),
        COALESCE(back_ratio, 0),
        COALESCE(stamina_ratio, 0),
        COALESCE(arms_ratio, 0),
        COALESCE(legs_ratio, 0),
        COALESCE(abs_ratio, 0)
    INTO
        chest_ratio,
        back_ratio,
        stamina_ratio,
        arms_ratio,
        legs_ratio,
        abs_ratio
    FROM exercise_stats_ratio
    WHERE id = NEW.exercise_stats_ratio_id;

    -- 경험치 계산 (1분당 5)
    SET exp_gain = FLOOR(NEW.exercise_duration / 60) * 5;

        -- user_character id 가져오기
    SELECT id INTO user_char_id
    FROM user_character
    WHERE user_id = NEW.user_id;

    -- 1. 경험치 증가
    UPDATE user_character
    SET user_experience = user_experience + exp_gain
    WHERE user_id = NEW.user_id;

    -- 2. 스탯 증가
    UPDATE user_stats
    SET
        chest_stats = chest_stats + (chest_ratio * NEW.exercise_cnt),
        back_stats = back_stats + (back_ratio * NEW.exercise_cnt),
        stamina_stats = stamina_stats + (stamina_ratio * NEW.exercise_cnt),
        arms_stats = arms_stats + (arms_ratio * NEW.exercise_cnt),
        legs_stats = legs_stats + (legs_ratio * NEW.exercise_cnt),
        abs_stats = abs_stats + (abs_ratio * NEW.exercise_cnt),
        updated_at = CURRENT_TIMESTAMP
    WHERE user_id = NEW.user_id;

    -- 3. 퀘스트 업데이트
    UPDATE quests
    SET
        real_cnt = real_cnt + NEW.exercise_cnt,
        quest_time = TIME(NEW.exercise_date)
    WHERE
        user_character_id = user_char_id
      AND exercise_id = NEW.exercise_stats_ratio_id
      AND quest_date = DATE(NEW.exercise_date);
END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `exercise_stats_ratio`
--

DROP TABLE IF EXISTS `exercise_stats_ratio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise_stats_ratio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise_type` varchar(50) NOT NULL,
  `chest_ratio` decimal(5,2) NOT NULL,
  `back_ratio` decimal(5,2) NOT NULL,
  `stamina_ratio` decimal(5,2) NOT NULL,
  `arms_ratio` decimal(5,2) NOT NULL,
  `legs_ratio` decimal(5,2) NOT NULL,
  `abs_ratio` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise_stats_ratio`
--

LOCK TABLES `exercise_stats_ratio` WRITE;
/*!40000 ALTER TABLE `exercise_stats_ratio` DISABLE KEYS */;
INSERT INTO `exercise_stats_ratio` VALUES (1,'pushup',0.40,0.05,0.20,0.30,0.05,0.05),(2,'squat',0.05,0.05,0.20,0.05,0.50,0.30),(3,'lunge',0.05,0.05,0.15,0.05,0.50,0.30),(4,'plank',0.05,0.05,0.45,0.05,0.20,0.45);
/*!40000 ALTER TABLE `exercise_stats_ratio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `friend_id` varchar(30) NOT NULL,
  `friend_nickname` varchar(15) DEFAULT NULL,
  `friend_level` int NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_friendship` (`user_id`,`friend_id`),
  KEY `fk_friend` (`friend_id`),
  CONSTRAINT `fk_friend` FOREIGN KEY (`friend_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `friends_chk_1` CHECK ((`status` in (_utf8mb4'pending',_utf8mb4'accepted',_utf8mb4'blocked')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_results`
--

DROP TABLE IF EXISTS `game_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_results` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `game_id` varchar(200) NOT NULL,
  `exercise_id` bigint NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `opponent_id` varchar(100) NOT NULL,
  `user_score` smallint NOT NULL,
  `opponent_score` smallint NOT NULL,
  `result` enum('WIN','LOSE','DRAW') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_results`
--

LOCK TABLES `game_results` WRITE;
/*!40000 ALTER TABLE `game_results` DISABLE KEYS */;
INSERT INTO `game_results` VALUES (1,'game-20250105-001',2,'ssafy123','kim456',25,20,'WIN','2025-01-05 05:30:00'),(2,'game-20250108-002',2,'ssafy123','lee789',18,22,'LOSE','2025-01-08 09:15:00'),(3,'game-20250112-003',2,'ssafy123','park234',30,25,'WIN','2025-01-12 01:45:00'),(4,'game-20250115-004',2,'ssafy123','choi567',22,22,'DRAW','2025-01-15 11:30:00'),(5,'game-20250120-005',2,'ssafy123','jung111',15,28,'LOSE','2025-01-20 07:00:00'),(6,'game-20250123-006',2,'ssafy123','kang222',27,25,'WIN','2025-01-23 10:10:00'),(7,'game-20250127-007',2,'ssafy123','yoon333',19,24,'LOSE','2025-01-27 06:45:00'),(8,'game-20250130-008',2,'ssafy123','shin444',26,18,'WIN','2025-01-30 03:20:00'),(9,'game-20250202-009',2,'ssafy123','song555',30,17,'WIN','2025-02-02 02:00:00'),(10,'game-20250205-010',2,'ssafy123','yang666',20,24,'LOSE','2025-02-05 08:30:00'),(11,'game-20250208-011',2,'ssafy123','han777',25,25,'DRAW','2025-02-08 04:45:00'),(12,'game-20250210-012',2,'ssafy123','oh888',28,22,'WIN','2025-02-10 09:30:00'),(13,'game-20250212-013',2,'ssafy123','seo999',19,26,'LOSE','2025-02-12 05:15:00'),(14,'game-20250215-014',2,'ssafy123','bae000',32,27,'WIN','2025-02-15 11:00:00'),(15,'game-20250218-015',2,'ssafy123','cha123',24,29,'LOSE','2025-02-18 07:45:00');
/*!40000 ALTER TABLE `game_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quests`
--

DROP TABLE IF EXISTS `quests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_character_id` bigint NOT NULL,
  `quest_date` date NOT NULL,
  `quest_time` time DEFAULT NULL,
  `exercise_id` bigint NOT NULL,
  `exercise_cnt` int unsigned NOT NULL,
  `real_cnt` int unsigned DEFAULT '0',
  `is_completed` tinyint(1) DEFAULT '0',
  `message` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_character_id` (`user_character_id`),
  KEY `exercise_id` (`exercise_id`),
  CONSTRAINT `quests_ibfk_1` FOREIGN KEY (`user_character_id`) REFERENCES `user_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `quests_ibfk_2` FOREIGN KEY (`exercise_id`) REFERENCES `exercise_stats_ratio` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quests`
--

LOCK TABLES `quests` WRITE;
/*!40000 ALTER TABLE `quests` DISABLE KEYS */;
INSERT INTO `quests` VALUES (1,1,'2025-02-20',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(2,2,'2025-02-20','00:04:32',2,7,2,0,'스쿼트를 7번 해주세요!'),(3,3,'2025-02-20','00:02:15',2,9,5,0,'스쿼트를 9번 해주세요!'),(4,4,'2025-02-20',NULL,2,5,0,0,'스쿼트를 5번 해주세요!'),(5,5,'2025-02-20','00:05:21',2,5,3,0,'스쿼트를 5번 해주세요!'),(6,6,'2025-02-20',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(7,7,'2025-02-20','00:03:48',2,9,6,0,'스쿼트를 9번 해주세요!'),(8,8,'2025-02-20',NULL,2,5,0,0,'스쿼트를 5번 해주세요!'),(9,9,'2025-02-20','00:01:55',2,7,4,0,'스쿼트를 7번 해주세요!'),(10,10,'2025-02-20','00:05:10',2,5,2,0,'스쿼트를 5번 해주세요!'),(11,11,'2025-02-20',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(12,12,'2025-02-20','00:03:22',2,9,7,0,'스쿼트를 9번 해주세요!'),(13,13,'2025-02-20',NULL,2,5,0,0,'스쿼트를 5번 해주세요!'),(14,14,'2025-02-20','00:02:50',2,7,3,0,'스쿼트를 7번 해주세요!'),(15,15,'2025-02-20','00:04:44',2,9,5,0,'스쿼트를 9번 해주세요!'),(16,16,'2025-02-20',NULL,2,5,0,0,'스쿼트를 5번 해주세요!'),(17,17,'2025-02-20','00:03:10',2,7,4,0,'스쿼트를 7번 해주세요!'),(18,18,'2025-02-20',NULL,2,5,0,0,'스쿼트를 5번 해주세요!'),(19,19,'2025-02-20','00:01:40',2,9,6,0,'스쿼트를 9번 해주세요!'),(20,20,'2025-02-20','00:02:59',2,7,2,0,'스쿼트를 7번 해주세요!'),(21,1,'2025-01-03','08:30:00',2,7,15,1,'스쿼트를 7번 해주세요!'),(22,1,'2025-01-04',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(23,1,'2025-01-05','18:45:00',2,7,60,1,'스쿼트를 7번 해주세요!'),(24,1,'2025-01-06',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(25,1,'2025-01-07',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(26,1,'2025-01-08','07:30:00',2,7,18,1,'스쿼트를 7번 해주세요!'),(27,1,'2025-01-09',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(28,1,'2025-01-10','19:00:00',2,7,20,1,'스쿼트를 7번 해주세요!'),(29,1,'2025-01-11',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(30,1,'2025-01-12','16:45:00',2,7,47,1,'스쿼트를 7번 해주세요!'),(31,1,'2025-01-13',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(32,1,'2025-01-14',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(33,1,'2025-01-15','08:00:00',2,7,15,1,'스쿼트를 7번 해주세요!'),(34,1,'2025-01-16',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(35,1,'2025-01-17','17:30:00',2,7,40,1,'스쿼트를 7번 해주세요!'),(36,1,'2025-01-18',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(37,1,'2025-01-19',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(38,1,'2025-01-20','06:45:00',2,7,22,1,'스쿼트를 7번 해주세요!'),(39,1,'2025-01-21',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(40,1,'2025-01-22','18:15:00',2,7,28,1,'스쿼트를 7번 해주세요!'),(41,1,'2025-01-23',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(42,1,'2025-01-24',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(43,1,'2025-01-25','07:30:00',2,7,16,1,'스쿼트를 7번 해주세요!'),(44,1,'2025-01-26',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(45,1,'2025-01-27','16:00:00',2,7,30,1,'스쿼트를 7번 해주세요!'),(46,1,'2025-01-28',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(47,1,'2025-01-29',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(48,1,'2025-01-30','18:45:00',2,7,45,1,'스쿼트를 7번 해주세요!'),(49,1,'2025-01-31',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(50,1,'2025-02-01','09:00:00',2,7,18,1,'스쿼트를 7번 해주세요!'),(51,1,'2025-02-02','17:30:00',2,7,22,1,'스쿼트를 7번 해주세요!'),(52,1,'2025-02-03',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(53,1,'2025-02-04','07:45:00',2,7,32,1,'스쿼트를 7번 해주세요!'),(54,1,'2025-02-05','18:30:00',2,7,16,1,'스쿼트를 7번 해주세요!'),(55,1,'2025-02-06',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(56,1,'2025-02-07',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(57,1,'2025-02-08','10:15:00',2,7,40,1,'스쿼트를 7번 해주세요!'),(58,1,'2025-02-09',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(59,1,'2025-02-10','19:00:00',2,7,60,1,'스쿼트를 7번 해주세요!'),(60,1,'2025-02-11',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(61,1,'2025-02-12','08:15:00',2,7,19,1,'스쿼트를 7번 해주세요!'),(62,1,'2025-02-13',NULL,2,7,4,0,'스쿼트를 7번 해주세요!'),(63,1,'2025-02-14',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(64,1,'2025-02-15','16:30:00',2,7,45,1,'스쿼트를 7번 해주세요!'),(65,1,'2025-02-16',NULL,2,7,0,0,'스쿼트를 7번 해주세요!'),(66,1,'2025-02-17','07:00:00',2,7,24,1,'스쿼트를 7번 해주세요!'),(67,1,'2025-02-18','19:45:00',2,7,30,1,'스쿼트를 7번 해주세요!');
/*!40000 ALTER TABLE `quests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutorial_types`
--

DROP TABLE IF EXISTS `tutorial_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tutorial_types` (
  `id` smallint NOT NULL AUTO_INCREMENT,
  `tutorial_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tutorial_name` (`tutorial_name`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutorial_types`
--

LOCK TABLES `tutorial_types` WRITE;
/*!40000 ALTER TABLE `tutorial_types` DISABLE KEYS */;
INSERT INTO `tutorial_types` VALUES (3,'lunge'),(98,'MainPage'),(4,'plank'),(1,'pushup'),(2,'squat'),(97,'Start'),(99,'UI');
/*!40000 ALTER TABLE `tutorial_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_character`
--

DROP TABLE IF EXISTS `user_character`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_character` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `user_nickname` varchar(15) NOT NULL,
  `gender` char(1) NOT NULL,
  `avatar` varchar(30) NOT NULL,
  `user_level` smallint unsigned NOT NULL DEFAULT '1',
  `user_experience` int NOT NULL DEFAULT '0',
  `points` smallint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `user_nickname` (`user_nickname`),
  CONSTRAINT `user_character_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_character_chk_1` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F'))),
  CONSTRAINT `user_character_chk_2` CHECK ((`user_level` <= 999)),
  CONSTRAINT `user_character_chk_3` CHECK ((`points` <= 50000))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_character`
--

LOCK TABLES `user_character` WRITE;
/*!40000 ALTER TABLE `user_character` DISABLE KEYS */;
INSERT INTO `user_character` VALUES (1,'ssafy123','GilDong','M','7-3-8',6,148,1000),(2,'kim456','ChulSoo123','M','2-6-4',3,62,500),(3,'lee789','YoungHee','F','1-8-10',7,184,1500),(4,'park234','JiSungSoccer','M','5-9-2',4,81,800),(5,'choi567','MinSooKing','M','4-7-1',2,98,300),(6,'jung111','SoHeeJjang','F','10-3-5',8,42,2000),(7,'kang222','DongWonOppa','M','6-2-8',6,182,1200),(8,'yoon333','SeoJunMan','M','9-4-1',9,25,2300),(9,'shin444','MinAhGirl','F','3-7-6',3,134,600),(10,'song555','HyeKyoNim','F','8-5-2',5,71,1100),(11,'yang666','HyunSeokBoss','M','7-4-8',6,95,1700),(12,'han777','JiMinLove','F','5-3-2',4,135,900),(13,'oh888','YeonSeoStar','F','2-9-6',7,145,1600),(14,'seo999','InGukRocks','M','1-6-4',5,117,1200),(15,'bae000','SuzyBae','F','9-8-5',3,103,700),(16,'cha123','EunWooOppa','M','6-7-3',6,131,1300),(17,'moon234','ChaeWonMoon','F','4-5-7',5,90,1100),(18,'joo345','JiHoonJjang','M','3-9-2',7,160,1800),(19,'ryu456','JunYeolBest','M','8-2-4',4,111,900),(20,'kwon567','SangWooPower','M','7-6-1',5,139,1400);
/*!40000 ALTER TABLE `user_character` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `create_user_stats` AFTER INSERT ON `user_character` FOR EACH ROW BEGIN
    INSERT INTO user_stats (
        user_id,
        arms_stats,
        legs_stats,
        chest_stats,
        abs_stats,
        back_stats,
        stamina_stats
    ) VALUES (
                 NEW.user_id,
                 10,  -- DEFAULT 값들
                 10,
                 10,
                 10,
                 10,
                 10
             );
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `create_user_rank_scores` AFTER INSERT ON `user_character` FOR EACH ROW BEGIN
    INSERT INTO user_rank_scores (user_id, exercise_id, rank_score)
    SELECT NEW.user_id, id, 1000  -- 기본 랭크 점수
    FROM exercise_stats_ratio;  -- 등록된 모든 운동 종류를 가져와 삽입
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `create_user_tutorial_progress` AFTER INSERT ON `user_character` FOR EACH ROW BEGIN
    -- tutorial_types 테이블의 모든 튜토리얼을 가져와서 user_tutorial_progress에 추가
    INSERT INTO user_tutorial_progress (user_id, tutorial_id, is_completed, completed_at)
    SELECT NEW.user_id, id, FALSE, NULL FROM tutorial_types;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `update_level_before_insert` BEFORE UPDATE ON `user_character` FOR EACH ROW BEGIN
    WHILE NEW.user_experience >= 200 DO
        SET NEW.user_experience = NEW.user_experience - 200;
        SET NEW.user_level = NEW.user_level + 1;
END WHILE;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_name` varchar(15) NOT NULL,
  `user_email` varchar(300) NOT NULL,
  `phone_number` varchar(13) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_temporary_pw` tinyint(1) DEFAULT '0',
  `is_valid` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `user_email` (`user_email`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'ssafy123','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Hong Gil-dong','hong123@gmail.com','010-1234-5678','2025-02-20 06:59:04',0,1),(2,'kim456','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Kim Chul-soo','kim456@naver.com','010-2345-6789','2025-02-20 06:59:04',0,1),(3,'lee789','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Lee Young-hee','lee789@daum.net','010-3456-7890','2025-02-20 06:59:04',0,1),(4,'park234','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Park Ji-sung','park234@gmail.com','010-4567-8901','2025-02-20 06:59:04',0,1),(5,'choi567','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Choi Min-soo','choi567@naver.com','010-5678-9012','2025-02-20 06:59:04',0,1),(6,'jung111','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Jung So-hee','jung111@gmail.com','010-6789-0123','2025-02-20 06:59:04',0,1),(7,'kang222','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Kang Dong-won','kang222@naver.com','010-7890-1234','2025-02-20 06:59:04',0,1),(8,'yoon333','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Yoon Seo-jun','yoon333@daum.net','010-8901-2345','2025-02-20 06:59:04',0,1),(9,'shin444','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Shin Min-ah','shin444@gmail.com','010-9012-3456','2025-02-20 06:59:04',0,1),(10,'song555','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Song Hye-kyo','song555@naver.com','010-0123-4567','2025-02-20 06:59:04',0,1),(11,'yang666','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Yang Hyun-suk','yang666@gmail.com','010-1111-2222','2025-02-20 06:59:04',0,1),(12,'han777','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Han Ji-min','han777@naver.com','010-2222-3333','2025-02-20 06:59:04',0,1),(13,'oh888','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Oh Yeon-seo','oh888@daum.net','010-3333-4444','2025-02-20 06:59:04',0,1),(14,'seo999','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Seo In-guk','seo999@gmail.com','010-4444-5555','2025-02-20 06:59:04',0,1),(15,'bae000','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Bae Suzy','bae000@naver.com','010-5555-6666','2025-02-20 06:59:04',0,1),(16,'cha123','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Cha Eun-woo','cha123@gmail.com','010-6666-7777','2025-02-20 06:59:04',0,1),(17,'moon234','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Moon Chae-won','moon234@naver.com','010-7777-8888','2025-02-20 06:59:04',0,1),(18,'joo345','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Joo Ji-hoon','joo345@daum.net','010-8888-9999','2025-02-20 06:59:04',0,1),(19,'ryu456','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Ryu Jun-yeol','ryu456@gmail.com','010-9999-0000','2025-02-20 06:59:04',0,1),(20,'kwon567','$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.','Kwon Sang-woo','kwon567@naver.com','010-0000-1111','2025-02-20 06:59:04',0,1);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_rank_scores`
--

DROP TABLE IF EXISTS `user_rank_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_rank_scores` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `exercise_id` bigint NOT NULL,
  `rank_score` smallint NOT NULL DEFAULT '1000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`exercise_id`),
  KEY `exercise_id` (`exercise_id`),
  CONSTRAINT `user_rank_scores_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_character` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_rank_scores_ibfk_2` FOREIGN KEY (`exercise_id`) REFERENCES `exercise_stats_ratio` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_rank_scores`
--

LOCK TABLES `user_rank_scores` WRITE;
/*!40000 ALTER TABLE `user_rank_scores` DISABLE KEYS */;
INSERT INTO `user_rank_scores` VALUES (1,'ssafy123',1,1148),(2,'ssafy123',2,1238),(3,'ssafy123',3,1080),(4,'ssafy123',4,1228),(5,'kim456',1,1706),(6,'kim456',2,1109),(7,'kim456',3,1347),(8,'kim456',4,1403),(9,'lee789',1,1020),(10,'lee789',2,1237),(11,'lee789',3,1789),(12,'lee789',4,1587),(13,'park234',1,866),(14,'park234',2,1728),(15,'park234',3,991),(16,'park234',4,1349),(17,'choi567',1,1177),(18,'choi567',2,1575),(19,'choi567',3,1633),(20,'choi567',4,1360),(21,'jung111',1,1782),(22,'jung111',2,1631),(23,'jung111',3,857),(24,'jung111',4,1217),(25,'kang222',1,1632),(26,'kang222',2,1250),(27,'kang222',3,940),(28,'kang222',4,1608),(29,'yoon333',1,1506),(30,'yoon333',2,1678),(31,'yoon333',3,1028),(32,'yoon333',4,1698),(33,'shin444',1,1301),(34,'shin444',2,1460),(35,'shin444',3,1399),(36,'shin444',4,1328),(37,'song555',1,912),(38,'song555',2,905),(39,'song555',3,938),(40,'song555',4,1047),(41,'yang666',1,869),(42,'yang666',2,1553),(43,'yang666',3,1699),(44,'yang666',4,887),(45,'han777',1,1132),(46,'han777',2,1468),(47,'han777',3,1470),(48,'han777',4,1455),(49,'oh888',1,862),(50,'oh888',2,1679),(51,'oh888',3,911),(52,'oh888',4,1476),(53,'seo999',1,1454),(54,'seo999',2,1186),(55,'seo999',3,892),(56,'seo999',4,912),(57,'bae000',1,954),(58,'bae000',2,1364),(59,'bae000',3,1355),(60,'bae000',4,878),(61,'cha123',1,1073),(62,'cha123',2,1212),(63,'cha123',3,1729),(64,'cha123',4,930),(65,'moon234',1,1068),(66,'moon234',2,819),(67,'moon234',3,1076),(68,'moon234',4,1085),(69,'joo345',1,1479),(70,'joo345',2,993),(71,'joo345',3,895),(72,'joo345',4,1619),(73,'ryu456',1,1590),(74,'ryu456',2,886),(75,'ryu456',3,1346),(76,'ryu456',4,1492),(77,'kwon567',1,1309),(78,'kwon567',2,1480),(79,'kwon567',3,1098),(80,'kwon567',4,1303);
/*!40000 ALTER TABLE `user_rank_scores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_stats`
--

DROP TABLE IF EXISTS `user_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `arms_stats` smallint unsigned NOT NULL DEFAULT '10',
  `legs_stats` smallint unsigned NOT NULL DEFAULT '10',
  `chest_stats` smallint unsigned NOT NULL DEFAULT '10',
  `abs_stats` smallint unsigned NOT NULL DEFAULT '10',
  `back_stats` smallint unsigned NOT NULL DEFAULT '10',
  `stamina_stats` smallint unsigned NOT NULL DEFAULT '10',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `user_stats_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_stats_chk_1` CHECK ((`arms_stats` <= 1000)),
  CONSTRAINT `user_stats_chk_2` CHECK ((`legs_stats` <= 1000)),
  CONSTRAINT `user_stats_chk_3` CHECK ((`chest_stats` <= 1000)),
  CONSTRAINT `user_stats_chk_4` CHECK ((`abs_stats` <= 1000)),
  CONSTRAINT `user_stats_chk_5` CHECK ((`back_stats` <= 1000)),
  CONSTRAINT `user_stats_chk_6` CHECK ((`stamina_stats` <= 1000))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_stats`
--

LOCK TABLES `user_stats` WRITE;
/*!40000 ALTER TABLE `user_stats` DISABLE KEYS */;
INSERT INTO `user_stats` VALUES (1,'ssafy123',124,971,515,640,659,375,'2025-02-20 06:59:05'),(2,'kim456',281,747,899,266,605,235,'2025-02-20 06:59:05'),(3,'lee789',989,945,758,944,455,427,'2025-02-20 06:59:05'),(4,'park234',517,305,952,869,486,804,'2025-02-20 06:59:05'),(5,'choi567',449,511,210,499,865,836,'2025-02-20 06:59:05'),(6,'jung111',418,317,323,654,310,570,'2025-02-20 06:59:05'),(7,'kang222',910,784,191,572,296,748,'2025-02-20 06:59:05'),(8,'yoon333',64,703,344,590,917,829,'2025-02-20 06:59:05'),(9,'shin444',461,125,221,721,951,605,'2025-02-20 06:59:05'),(10,'song555',971,394,41,990,860,330,'2025-02-20 06:59:05'),(11,'yang666',248,85,662,75,358,569,'2025-02-20 06:59:05'),(12,'han777',147,401,562,610,364,970,'2025-02-20 06:59:05'),(13,'oh888',982,580,935,945,922,773,'2025-02-20 06:59:05'),(14,'seo999',150,441,755,461,33,754,'2025-02-20 06:59:05'),(15,'bae000',184,824,588,458,518,215,'2025-02-20 06:59:05'),(16,'cha123',868,842,608,505,692,946,'2025-02-20 06:59:05'),(17,'moon234',469,345,308,494,549,261,'2025-02-20 06:59:05'),(18,'joo345',421,180,618,559,933,998,'2025-02-20 06:59:05'),(19,'ryu456',981,328,666,357,769,783,'2025-02-20 06:59:05'),(20,'kwon567',686,603,948,941,864,496,'2025-02-20 06:59:05');
/*!40000 ALTER TABLE `user_stats` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`ssafy`@`%`*/ /*!50003 TRIGGER `after_user_stats_update` AFTER UPDATE ON `user_stats` FOR EACH ROW BEGIN
    DECLARE stats_history_exists INT;
    DECLARE user_char_id BIGINT;

    -- user_character id 가져오기
    SELECT id INTO user_char_id
    FROM user_character
    WHERE user_id = NEW.user_id;

    -- 오늘 날짜의 히스토리 존재 여부 확인
    SELECT COUNT(*) INTO stats_history_exists
    FROM user_stats_history
    WHERE user_character_id = user_char_id 
    AND stats_date = CURRENT_DATE;

    IF stats_history_exists = 0 THEN
        -- 해당 날짜의 기록이 없으면 새로 INSERT
        INSERT INTO user_stats_history (
            user_character_id,
            chest_stats,
            back_stats,
            stamina_stats,
            arms_stats,
            legs_stats,
            abs_stats,
            stats_date
        ) VALUES (
            user_char_id,
            NEW.chest_stats,
            NEW.back_stats,
            NEW.stamina_stats,
            NEW.arms_stats,
            NEW.legs_stats,
            NEW.abs_stats,
            CURRENT_DATE
        );
    ELSE
        -- 해당 날짜의 기록이 있으면 UPDATE
        UPDATE user_stats_history
        SET
            chest_stats = NEW.chest_stats,
            back_stats = NEW.back_stats,
            stamina_stats = NEW.stamina_stats,
            arms_stats = NEW.arms_stats,
            legs_stats = NEW.legs_stats,
            abs_stats = NEW.abs_stats,
            updated_at = CURRENT_TIMESTAMP
        WHERE 
            user_character_id = user_char_id
            AND stats_date = CURRENT_DATE;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_stats_history`
--

DROP TABLE IF EXISTS `user_stats_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_stats_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_character_id` bigint NOT NULL,
  `arms_stats` smallint unsigned NOT NULL,
  `legs_stats` smallint unsigned NOT NULL,
  `chest_stats` smallint unsigned NOT NULL,
  `abs_stats` smallint unsigned NOT NULL,
  `back_stats` smallint unsigned NOT NULL,
  `stamina_stats` smallint unsigned NOT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `stats_date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_date_user` (`user_character_id`,`stats_date`),
  CONSTRAINT `user_stats_history_ibfk_1` FOREIGN KEY (`user_character_id`) REFERENCES `user_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_stats_history`
--

LOCK TABLES `user_stats_history` WRITE;
/*!40000 ALTER TABLE `user_stats_history` DISABLE KEYS */;
INSERT INTO `user_stats_history` VALUES (1,1,124,971,515,640,659,375,'2025-02-20 06:59:05','2025-02-20'),(2,2,281,747,899,266,605,235,'2025-02-20 06:59:05','2025-02-20'),(3,3,989,945,758,944,455,427,'2025-02-20 06:59:05','2025-02-20'),(4,4,517,305,952,869,486,804,'2025-02-20 06:59:05','2025-02-20'),(5,5,449,511,210,499,865,836,'2025-02-20 06:59:05','2025-02-20'),(6,6,418,317,323,654,310,570,'2025-02-20 06:59:05','2025-02-20'),(7,7,910,784,191,572,296,748,'2025-02-20 06:59:05','2025-02-20'),(8,8,64,703,344,590,917,829,'2025-02-20 06:59:05','2025-02-20'),(9,9,461,125,221,721,951,605,'2025-02-20 06:59:05','2025-02-20'),(10,10,971,394,41,990,860,330,'2025-02-20 06:59:05','2025-02-20'),(11,11,248,85,662,75,358,569,'2025-02-20 06:59:05','2025-02-20'),(12,12,147,401,562,610,364,970,'2025-02-20 06:59:05','2025-02-20'),(13,13,982,580,935,945,922,773,'2025-02-20 06:59:05','2025-02-20'),(14,14,150,441,755,461,33,754,'2025-02-20 06:59:05','2025-02-20'),(15,15,184,824,588,458,518,215,'2025-02-20 06:59:05','2025-02-20'),(16,16,868,842,608,505,692,946,'2025-02-20 06:59:05','2025-02-20'),(17,17,469,345,308,494,549,261,'2025-02-20 06:59:05','2025-02-20'),(18,18,421,180,618,559,933,998,'2025-02-20 06:59:05','2025-02-20'),(19,19,981,328,666,357,769,783,'2025-02-20 06:59:05','2025-02-20'),(20,20,686,603,948,941,864,496,'2025-02-20 06:59:05','2025-02-20'),(21,1,200,250,220,180,190,210,'2025-01-02 14:59:59','2025-01-02'),(22,1,201,258,221,185,191,213,'2025-01-03 11:00:00','2025-01-03'),(23,1,204,288,224,203,194,225,'2025-01-05 11:00:00','2025-01-05'),(24,1,205,297,225,208,195,229,'2025-01-08 11:00:00','2025-01-08'),(25,1,206,307,226,214,196,233,'2025-01-10 11:00:00','2025-01-10'),(26,1,208,330,228,228,198,242,'2025-01-12 11:00:00','2025-01-12'),(27,1,209,338,229,233,199,245,'2025-01-15 11:00:00','2025-01-15'),(28,1,211,358,231,245,201,253,'2025-01-17 11:00:00','2025-01-17'),(29,1,212,369,232,251,202,257,'2025-01-20 11:00:00','2025-01-20'),(30,1,213,383,233,260,203,263,'2025-01-22 11:00:00','2025-01-22'),(31,1,214,391,234,264,204,266,'2025-01-25 11:00:00','2025-01-25'),(32,1,216,406,236,273,206,272,'2025-01-27 11:00:00','2025-01-27'),(33,1,218,428,238,287,208,281,'2025-01-30 11:00:00','2025-01-30'),(34,1,219,437,239,292,209,285,'2025-02-01 11:00:00','2025-02-01'),(35,1,220,448,240,299,210,289,'2025-02-02 11:00:00','2025-02-02'),(36,1,221,464,241,308,211,296,'2025-02-04 11:00:00','2025-02-04'),(37,1,222,472,242,313,212,299,'2025-02-05 11:00:00','2025-02-05'),(38,1,224,492,244,325,214,307,'2025-02-08 11:00:00','2025-02-08'),(39,1,227,522,247,343,217,319,'2025-02-10 11:00:00','2025-02-10'),(40,1,228,532,248,349,218,323,'2025-02-12 11:00:00','2025-02-12'),(41,1,230,554,250,362,220,332,'2025-02-15 11:00:00','2025-02-15'),(42,1,232,566,252,370,222,336,'2025-02-17 11:00:00','2025-02-17'),(43,1,233,581,253,379,223,342,'2025-02-18 11:00:00','2025-02-18');
/*!40000 ALTER TABLE `user_stats_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tutorial_progress`
--

DROP TABLE IF EXISTS `user_tutorial_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tutorial_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(30) NOT NULL,
  `tutorial_id` smallint NOT NULL,
  `is_completed` tinyint(1) DEFAULT '0',
  `completed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tutorial_id` (`tutorial_id`),
  CONSTRAINT `user_tutorial_progress_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `user_tutorial_progress_ibfk_2` FOREIGN KEY (`tutorial_id`) REFERENCES `tutorial_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tutorial_progress`
--

LOCK TABLES `user_tutorial_progress` WRITE;
/*!40000 ALTER TABLE `user_tutorial_progress` DISABLE KEYS */;
INSERT INTO `user_tutorial_progress` VALUES (1,'ssafy123',3,0,NULL),(2,'ssafy123',98,0,NULL),(3,'ssafy123',4,0,NULL),(4,'ssafy123',1,0,NULL),(5,'ssafy123',2,0,NULL),(6,'ssafy123',97,0,NULL),(7,'ssafy123',99,0,NULL),(8,'kim456',3,0,NULL),(9,'kim456',98,0,NULL),(10,'kim456',4,0,NULL),(11,'kim456',1,0,NULL),(12,'kim456',2,0,NULL),(13,'kim456',97,0,NULL),(14,'kim456',99,0,NULL),(15,'lee789',3,0,NULL),(16,'lee789',98,0,NULL),(17,'lee789',4,0,NULL),(18,'lee789',1,0,NULL),(19,'lee789',2,0,NULL),(20,'lee789',97,0,NULL),(21,'lee789',99,0,NULL),(22,'park234',3,0,NULL),(23,'park234',98,0,NULL),(24,'park234',4,0,NULL),(25,'park234',1,0,NULL),(26,'park234',2,0,NULL),(27,'park234',97,0,NULL),(28,'park234',99,0,NULL),(29,'choi567',3,0,NULL),(30,'choi567',98,0,NULL),(31,'choi567',4,0,NULL),(32,'choi567',1,0,NULL),(33,'choi567',2,0,NULL),(34,'choi567',97,0,NULL),(35,'choi567',99,0,NULL),(36,'jung111',3,0,NULL),(37,'jung111',98,0,NULL),(38,'jung111',4,0,NULL),(39,'jung111',1,0,NULL),(40,'jung111',2,0,NULL),(41,'jung111',97,0,NULL),(42,'jung111',99,0,NULL),(43,'kang222',3,0,NULL),(44,'kang222',98,0,NULL),(45,'kang222',4,0,NULL),(46,'kang222',1,0,NULL),(47,'kang222',2,0,NULL),(48,'kang222',97,0,NULL),(49,'kang222',99,0,NULL),(50,'yoon333',3,0,NULL),(51,'yoon333',98,0,NULL),(52,'yoon333',4,0,NULL),(53,'yoon333',1,0,NULL),(54,'yoon333',2,0,NULL),(55,'yoon333',97,0,NULL),(56,'yoon333',99,0,NULL),(57,'shin444',3,0,NULL),(58,'shin444',98,0,NULL),(59,'shin444',4,0,NULL),(60,'shin444',1,0,NULL),(61,'shin444',2,0,NULL),(62,'shin444',97,0,NULL),(63,'shin444',99,0,NULL),(64,'song555',3,0,NULL),(65,'song555',98,0,NULL),(66,'song555',4,0,NULL),(67,'song555',1,0,NULL),(68,'song555',2,0,NULL),(69,'song555',97,0,NULL),(70,'song555',99,0,NULL),(71,'yang666',3,0,NULL),(72,'yang666',98,0,NULL),(73,'yang666',4,0,NULL),(74,'yang666',1,0,NULL),(75,'yang666',2,0,NULL),(76,'yang666',97,0,NULL),(77,'yang666',99,0,NULL),(78,'han777',3,0,NULL),(79,'han777',98,0,NULL),(80,'han777',4,0,NULL),(81,'han777',1,0,NULL),(82,'han777',2,0,NULL),(83,'han777',97,0,NULL),(84,'han777',99,0,NULL),(85,'oh888',3,0,NULL),(86,'oh888',98,0,NULL),(87,'oh888',4,0,NULL),(88,'oh888',1,0,NULL),(89,'oh888',2,0,NULL),(90,'oh888',97,0,NULL),(91,'oh888',99,0,NULL),(92,'seo999',3,0,NULL),(93,'seo999',98,0,NULL),(94,'seo999',4,0,NULL),(95,'seo999',1,0,NULL),(96,'seo999',2,0,NULL),(97,'seo999',97,0,NULL),(98,'seo999',99,0,NULL),(99,'bae000',3,0,NULL),(100,'bae000',98,0,NULL),(101,'bae000',4,0,NULL),(102,'bae000',1,0,NULL),(103,'bae000',2,0,NULL),(104,'bae000',97,0,NULL),(105,'bae000',99,0,NULL),(106,'cha123',3,0,NULL),(107,'cha123',98,0,NULL),(108,'cha123',4,0,NULL),(109,'cha123',1,0,NULL),(110,'cha123',2,0,NULL),(111,'cha123',97,0,NULL),(112,'cha123',99,0,NULL),(113,'moon234',3,0,NULL),(114,'moon234',98,0,NULL),(115,'moon234',4,0,NULL),(116,'moon234',1,0,NULL),(117,'moon234',2,0,NULL),(118,'moon234',97,0,NULL),(119,'moon234',99,0,NULL),(120,'joo345',3,0,NULL),(121,'joo345',98,0,NULL),(122,'joo345',4,0,NULL),(123,'joo345',1,0,NULL),(124,'joo345',2,0,NULL),(125,'joo345',97,0,NULL),(126,'joo345',99,0,NULL),(127,'ryu456',3,0,NULL),(128,'ryu456',98,0,NULL),(129,'ryu456',4,0,NULL),(130,'ryu456',1,0,NULL),(131,'ryu456',2,0,NULL),(132,'ryu456',97,0,NULL),(133,'ryu456',99,0,NULL),(134,'kwon567',3,0,NULL),(135,'kwon567',98,0,NULL),(136,'kwon567',4,0,NULL),(137,'kwon567',1,0,NULL),(138,'kwon567',2,0,NULL),(139,'kwon567',97,0,NULL),(140,'kwon567',99,0,NULL);
/*!40000 ALTER TABLE `user_tutorial_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'e103_db'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `daily_quest_creation` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`ssafy`@`%`*/ /*!50106 EVENT `daily_quest_creation` ON SCHEDULE EVERY 1 DAY STARTS '2025-02-21 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    -- 모든 사용자에 대해 스쿼트 퀘스트만 생성
INSERT INTO quests (user_character_id, quest_date, exercise_id, exercise_cnt, real_cnt, message)
SELECT
    uc.id,
    CURRENT_DATE,
    2,  -- 스쿼트의 exercise_id
    calculate_exercise_count(uc.user_level),
    0,  -- real_cnt 초기값
    CONCAT('스쿼트를 ',
           calculate_exercise_count(uc.user_level),
           '번 해주세요!')
FROM user_character uc;
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `quest_completion_check` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`ssafy`@`%`*/ /*!50106 EVENT `quest_completion_check` ON SCHEDULE EVERY 1 DAY STARTS '2025-02-21 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    -- 전날 퀘스트의 달성 여부 업데이트
UPDATE quests
SET is_completed = CASE
                       WHEN real_cnt >= exercise_cnt THEN TRUE
                       ELSE FALSE
    END
WHERE quest_date = DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY);
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'e103_db'
--
/*!50003 DROP FUNCTION IF EXISTS `calculate_exercise_count` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`ssafy`@`%` FUNCTION `calculate_exercise_count`(user_level INT) RETURNS int
    DETERMINISTIC
BEGIN
    -- 기본 횟수 5회, 5레벨 단위로 2회씩 증가
    RETURN 5 + (2 * FLOOR(user_level / 5));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `best_records`
--

/*!50001 DROP VIEW IF EXISTS `best_records`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`ssafy`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `best_records` AS select `el`.`user_id` AS `user_id`,`esr`.`exercise_type` AS `exercise_type`,max((case when (`el`.`exercise_duration` = 60) then `el`.`exercise_cnt` end)) AS `best_count_1min`,max((case when (`el`.`exercise_duration` = 120) then `el`.`exercise_cnt` end)) AS `best_count_2min`,max((case when (`el`.`exercise_duration` = 300) then `el`.`exercise_cnt` end)) AS `best_count_5min`,max((case when (`el`.`exercise_duration` = 600) then `el`.`exercise_cnt` end)) AS `best_count_10min` from (`exercise_log` `el` join `exercise_stats_ratio` `esr` on((`el`.`exercise_stats_ratio_id` = `esr`.`id`))) group by `el`.`user_id`,`esr`.`exercise_type` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-21  0:33:15
