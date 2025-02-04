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
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_valid boolean not null default 1,
                            is_temporary_pw boolean not null default 0
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

-- 운동 종류별 점수 비율
CREATE TABLE exercise_stats_ratio (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      exercise_type VARCHAR(50) NOT NULL,  -- 운동 종류 (스쿼트, 푸쉬업 등)
                                      chest_ratio DECIMAL(5, 2) NOT NULL,  -- 가슴에 대한 비율
                                      back_ratio DECIMAL(5, 2) NOT NULL,   -- 등 근육에 대한 비율
                                      stamina_ratio DECIMAL(5, 2) NOT NULL,  -- 체력에 대한 비율
                                      arms_ratio DECIMAL(5, 2) NOT NULL,  -- 팔 근육에 대한 비율
                                      legs_ratio DECIMAL(5, 2) NOT NULL,  -- 다리 근육에 대한 비율
                                      abs_ratio DECIMAL(5, 2) NOT NULL  -- 복근에 대한 비율
);

-- 운동 종류별 점수 비율 데이터
INSERT INTO exercise_stats_ratio (exercise_type, chest_ratio, back_ratio, stamina_ratio, arms_ratio, legs_ratio, abs_ratio)
VALUES
    ('푸시업', 0.40, 0.05, 0.20, 0.30, 0.05, 0.05),
    ('스쿼트', 0.05, 0.05, 0.20, 0.05, 0.50, 0.30),
    ('런지', 0.05, 0.05, 0.15, 0.05, 0.50, 0.30),
    ('플랭크', 0.05, 0.05, 0.50, 0.05, 0.05, 0.40);

-- 운동 기록 테이블 (user_character.id를 외래 키로 사용)
CREATE TABLE exercise_log (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id VARCHAR(30) NOT NULL,  -- user_character 테이블의 id 외래 키
                              exercise_duration INT UNSIGNED NOT NULL,  -- 운동 시간 (초 단위)
                              exercise_cnt INT UNSIGNED NOT NULL,  -- 운동 횟수 (예: 푸시업 횟수, 스쿼트 횟수 등)
                              exercise_stats_ratio_id BIGINT NOT NULL,  -- exercise_stats_ratio 테이블과 연결
                              exercise_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 운동 일시
                              FOREIGN KEY (user_id) REFERENCES user_character(id) ON DELETE CASCADE,
                              FOREIGN KEY (exercise_stats_ratio_id) REFERENCES exercise_stats_ratio(id) ON DELETE CASCADE
);