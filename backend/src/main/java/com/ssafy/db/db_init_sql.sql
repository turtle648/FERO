DROP DATABASE IF EXISTS E103_DB;

CREATE DATABASE E103_DB;

USE E103_DB;


-- 유저에 대한 정보
CREATE TABLE user_info (
                           id Bigint PRIMARY KEY AUTO_INCREMENT,
                           user_id VARCHAR(30) NOT NULL UNIQUE,
                           user_password VARCHAR(100) NOT NULL,
                           user_name VARCHAR(15) NOT NULL,
                           user_email VARCHAR(300) NOT NULL UNIQUE,
                           phone_number VARCHAR(13) NOT NULL UNIQUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_temporary_pw boolean default 0,
                           is_valid boolean default 1
);


-- 유저 캐릭터 정보
CREATE TABLE user_character (
                                id Bigint PRIMARY KEY AUTO_INCREMENT,
                                user_id VARCHAR(30) NOT NULL UNIQUE,
                                user_nickname VARCHAR(15) NOT NULL UNIQUE,
                                gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),

                                user_rank_score SMALLINT NOT NULL DEFAULT '1000',
                                user_level SMALLINT UNSIGNED NOT NULL DEFAULT 1 CHECK (user_level <= 100),
                                user_experience INT NOT NULL DEFAULT 0,

                                points SMALLINT UNSIGNED NOT NULL DEFAULT 0 CHECK (points <= 50000),
                                FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
);


-- 유저 스테이터스
CREATE TABLE user_stats(
                           id Bigint PRIMARY KEY AUTO_INCREMENT,
                           user_id VARCHAR(30) NOT NULL UNIQUE,

    -- 신체 부위별 스테이터스 컬럼
                           arms_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (arms_stats <= 1000),
                           legs_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (legs_stats <= 1000),
                           chest_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (chest_stats <= 1000),
                           abs_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (abs_stats <= 1000),
                           back_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (back_stats <= 1000),
                           stamina_stats SMALLINT UNSIGNED NOT NULL DEFAULT 10 CHECK (stamina_stats <= 1000),

                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
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


-- 운동 기록 테이블 (user_character.user_id를 외래 키로 사용)
CREATE TABLE exercise_log (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id VARCHAR(30) NOT NULL,  -- user_character 테이블의 id 외래 키
                              exercise_duration INT UNSIGNED NOT NULL,  -- 운동 시간 (초 단위)
                              exercise_cnt INT UNSIGNED NOT NULL,  -- 운동 횟수 (예: 푸시업 횟수, 스쿼트 횟수 등)
                              exercise_stats_ratio_id BIGINT NOT NULL,  -- exercise_stats_ratio 테이블과 연결
                              exercise_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 운동 일시
                              FOREIGN KEY (user_id) REFERENCES user_character(user_id) ON DELETE CASCADE,
                              FOREIGN KEY (exercise_stats_ratio_id) REFERENCES exercise_stats_ratio(id) ON DELETE CASCADE
);


-- 운동 종목, 시간별 최고 스코어를 나타내는 view
CREATE VIEW best_records AS
SELECT
    el.user_id,
    esr.exercise_type,
    MAX(CASE WHEN el.exercise_duration = 60 THEN el.exercise_cnt END) as best_count_1min,
    MAX(CASE WHEN el.exercise_duration = 120 THEN el.exercise_cnt END) as best_count_2min,
    MAX(CASE WHEN el.exercise_duration = 300 THEN el.exercise_cnt END) as best_count_5min,
    MAX(CASE WHEN el.exercise_duration = 600 THEN el.exercise_cnt END) as best_count_10min
FROM
    exercise_log el
        JOIN exercise_stats_ratio esr ON el.exercise_stats_ratio_id = esr.id
GROUP BY
    el.user_id, esr.exercise_type;


-- 튜토리얼 종류에 대한 테이블
CREATE TABLE tutorial_types (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                tutorial_name VARCHAR(50) NOT NULL UNIQUE  -- 'UI 기본', '운동 기록', '채팅', '푸시업' 등
);


-- 유저가 어떤 튜토리얼을 완료했는지에 대한 테이블
CREATE TABLE user_tutorial_progress (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        user_id VARCHAR(30) NOT NULL,
                                        tutorial_id BIGINT NOT NULL,
                                        is_completed BOOLEAN DEFAULT FALSE,
                                        completed_at TIMESTAMP,
                                        FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE,
                                        FOREIGN KEY (tutorial_id) REFERENCES tutorial_types(id)
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


-- -- -- -- -- 트리거 작업 -- -- -- -- --


-- 유저 캐릭터 생성하면 자동으로 스테이터스 정보 생성
DELIMITER //
CREATE TRIGGER create_user_stats
    AFTER INSERT ON user_character
    FOR EACH ROW
BEGIN
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
END //
DELIMITER ;


-- 경험치에 대한 레벨업 트리거 작업
DELIMITER //

CREATE TRIGGER update_level_before_insert BEFORE UPDATE ON user_character
    FOR EACH ROW
BEGIN
    WHILE NEW.user_experience >= 200 DO
        SET NEW.user_experience = NEW.user_experience - 200;
        SET NEW.user_level = NEW.user_level + 1;
END WHILE;
END;
//

DELIMITER ;

-- 마지막 운동날짜 8일 초과 유저 스탯 5씩 감소
-- 2주이상 안 한 사람 스탯 10으로 초기화
-- 이벤트 스케줄러로 매일 자정 시행
SET GLOBAL event_scheduler = ON;

DELIMITER //
CREATE EVENT decrease_and_reset_stats_event
ON SCHEDULE EVERY 1 DAY STARTS TIMESTAMP(CURRENT_DATE, '00:00:00')
DO
BEGIN
    -- 2주(14일) 초과 운동 안 한 유저: 스탯을 10으로 초기화
UPDATE user_stats
SET
    arms_stats = 10,
    legs_stats = 10,
    chest_stats = 10,
    abs_stats = 10,
    back_stats = 10,
    stamina_stats = 10,
    updated_at = CURRENT_TIMESTAMP
WHERE updated_at < NOW() - INTERVAL 14 DAY;

-- 8일 ~ 14일 사이 운동 안 한 유저: 스탯 -5 감소 (최소값 10 유지)
UPDATE user_stats
SET
    arms_stats = GREATEST(arms_stats - 5, 10),
    legs_stats = GREATEST(legs_stats - 5, 10),
    chest_stats = GREATEST(chest_stats - 5, 10),
    abs_stats = GREATEST(abs_stats - 5, 10),
    back_stats = GREATEST(back_stats - 5, 10),
    stamina_stats = GREATEST(stamina_stats - 5, 10),
    updated_at = CURRENT_TIMESTAMP
WHERE updated_at BETWEEN NOW() - INTERVAL 14 DAY AND NOW() - INTERVAL 8 DAY;
END;
//
DELIMITER ;


-- -- -- -- -- 데이터 삽입 -- -- -- -- --

-- 운동 종류별 점수 비율 데이터
INSERT INTO exercise_stats_ratio (exercise_type, chest_ratio, back_ratio, stamina_ratio, arms_ratio, legs_ratio, abs_ratio)
VALUES
    ('푸시업', 0.40, 0.05, 0.20, 0.30, 0.05, 0.05),
    ('스쿼트', 0.05, 0.05, 0.20, 0.05, 0.50, 0.30),
    ('런지', 0.05, 0.05, 0.15, 0.05, 0.50, 0.30),
    ('플랭크', 0.05, 0.05, 0.45, 0.05, 0.20, 0.45);


-- 기본 튜토리얼 데이터 삽입
INSERT INTO tutorial_types (tutorial_name) VALUES
                                               ('UI 기본'),
                                               ('운동 기록'),
                                               ('채팅'),
                                               ('푸시업'),
                                               ('스쿼트'),
                                               ('런지'),
                                               ('플랭크');
