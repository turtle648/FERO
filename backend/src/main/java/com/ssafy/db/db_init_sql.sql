DROP DATABASE IF EXISTS E103_DB;

CREATE DATABASE E103_DB;

USE E103_DB;

CREATE TABLE user_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(30) NOT NULL UNIQUE,
    user_password VARCHAR(100) NOT NULL,
    user_name VARCHAR(15) NOT NULL,
    user_email VARCHAR(300) NOT NULL UNIQUE,
    phone_number VARCHAR(13) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_character (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(30) NOT NULL UNIQUE,
    user_nickname VARCHAR(15) NOT NULL UNIQUE,
    gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
    pushup_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (pushup_best_record <= 1000),
    squat_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (squat_best_record <= 1000),
    pullup_best_record SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (pullup_best_record <= 1000),
    points SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (points <= 50000),
    FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);

CREATE TABLE chat_message (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id VARCHAR(30) NOT NULL,
    receiver_id VARCHAR(30) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES user_info(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);
