DROP DATABASE IF EXISTS E103_DB;

CREATE DATABASE E103_DB;

USE E103_DB;

CREATE TABLE user_info (
                           id Bigint PRIMARY KEY AUTO_INCREMENT,
                           user_id VARCHAR(30) NOT NULL UNIQUE,
                           user_password VARCHAR(100) NOT NULL,
                           user_name VARCHAR(15) NOT NULL,
                           user_email VARCHAR(300) NOT NULL UNIQUE,
                           phone_number VARCHAR(13) NOT NULL UNIQUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_character (
                                id Bigint PRIMARY KEY AUTO_INCREMENT,
                                user_id VARCHAR(30) NOT NULL UNIQUE,
                                user_nickname VARCHAR(15) NOT NULL UNIQUE,
                                gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
                                pushup_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (pushup_best_record <= 1000),
                                squat_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (squat_best_record <= 1000),
                                pullup_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (pullup_best_record <= 1000),
                                points SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (points <= 50000),
                                FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);

CREATE TABLE chat_room (
                           id Bigint PRIMARY KEY AUTO_INCREMENT,
                           user_id_1 VARCHAR(30) NOT NULL,
                           user_id_2 VARCHAR(30) NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id_1) REFERENCES user_info(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (user_id_2) REFERENCES user_info(user_id) ON DELETE CASCADE,
                           UNIQUE (user_id_1, user_id_2) -- 동일한 두 사람이 중복 생성 방지
);

CREATE TABLE chat_message (
                              id Bigint PRIMARY KEY AUTO_INCREMENT,
                              room_id BigInt NOT NULL,
                              sender_id VARCHAR(30) NOT NULL,
                              message TEXT NOT NULL,
                              sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
                              FOREIGN KEY (sender_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);
