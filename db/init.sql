-- DROP DATABASE IF EXISTS E103_DB;

-- CREATE DATABASE E103_DB;

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
                                avatar VARCHAR(30) NOT NULL,

                                user_level SMALLINT UNSIGNED NOT NULL DEFAULT 1 CHECK (user_level <= 999),
                                user_experience INT NOT NULL DEFAULT 0,

                                user_rank VARCHAR(12) DEFAULT "-"
                                    CHECK (user_rank IN ('Bronze', 'Silver', 'Gold', 'Diamond', '-')),

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


-- 유저의 각 운동별 랭크 점수
CREATE TABLE user_rank_scores (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  user_id VARCHAR(30) NOT NULL,
                                  exercise_id BIGINT NOT NULL,  -- exercise_stats_ratio의 id를 참조
                                  rank_score SMALLINT NOT NULL DEFAULT 1000,
                                  FOREIGN KEY (user_id) REFERENCES user_character(user_id) ON DELETE CASCADE,
                                  FOREIGN KEY (exercise_id) REFERENCES exercise_stats_ratio(id) ON DELETE RESTRICT,
                                  UNIQUE (user_id, exercise_id)  -- 동일 유저의 동일 운동에 대한 중복 데이터 방지
);


-- 운동 기록 테이블 (user_character.id를 외래 키로 사용)
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


-- 게임 결과에 대한 테이블(한 게임당 user1, user2 각각 기준으로 하나씩 생성)
CREATE TABLE game_results (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              game_id VARCHAR(200) NOT NULL,         -- 동일한 경기 기록을 식별하는 gameId
                              exercise_id BIGINT NOT NULL,          -- 운동 종류 ID
                              user_id VARCHAR(30) NOT NULL,              -- 유저 ID
                              opponent_id VARCHAR(100) NOT NULL,          -- 상대방 ID
                              user_score SMALLINT NOT NULL,              -- 유저의 점수
                              opponent_score SMALLINT NOT NULL,          -- 상대방 점수
                              result ENUM('WIN', 'LOSE', 'DRAW') NOT NULL, -- 유저 기준 결과
                              duration SMALLINT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              INDEX idx_user_id (user_id)           -- 유저별 조회 최적화
);


-- 튜토리얼 종류에 대한 테이블
CREATE TABLE tutorial_types (
                                id SMALLINT PRIMARY KEY AUTO_INCREMENT,
                                tutorial_name VARCHAR(50) NOT NULL UNIQUE  -- 'UI 기본', '운동 기록', '채팅', '푸시업' 등
);


-- 유저가 어떤 튜토리얼을 완료했는지에 대한 테이블
CREATE TABLE user_tutorial_progress (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        user_id VARCHAR(30) NOT NULL,
                                        tutorial_id SMALLINT NOT NULL,
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

-- 유저간의 친구정보에 대한 테이블
CREATE TABLE friends (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         user_id VARCHAR(30) NOT NULL,
                         friend_id VARCHAR(30) NOT NULL,
                         friend_nickname VARCHAR(15),
                         friend_level INT NOT NULL,
                         status VARCHAR(10) CHECK (status IN ('pending', 'accepted', 'blocked')),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE,
                         CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES user_info(user_id) ON DELETE CASCADE,
                         UNIQUE KEY unique_friendship (user_id, friend_id)
);

CREATE TABLE quests (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_character_id BIGINT NOT NULL,
                        quest_date DATE NOT NULL,          -- 날짜만 저장 (YYYY-MM-DD)
                        quest_time TIME,          -- 시간만 저장 (HH:MM:SS)
                        exercise_id BIGINT NOT NULL,       -- exercise_stats_ratio의 id 참조
                        exercise_cnt INT UNSIGNED NOT NULL, -- 목표 운동 횟수
                        real_cnt int unsigned default 0, -- 진짜 운동 횟수
                        is_completed BOOLEAN DEFAULT FALSE, -- 달성 여부
                        message VARCHAR(200),              -- 퀘스트 메시지

                        FOREIGN KEY (user_character_id) REFERENCES user_character(id) ON DELETE CASCADE,
                        FOREIGN KEY (exercise_id) REFERENCES exercise_stats_ratio(id) ON DELETE RESTRICT
);

-- 사용자의 스탯 히스토리
CREATE TABLE user_stats_history (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    user_character_id BIGINT NOT NULL,
                                    arms_stats SMALLINT UNSIGNED NOT NULL,
                                    legs_stats SMALLINT UNSIGNED NOT NULL,
                                    chest_stats SMALLINT UNSIGNED NOT NULL,
                                    abs_stats SMALLINT UNSIGNED NOT NULL,
                                    back_stats SMALLINT UNSIGNED NOT NULL,
                                    stamina_stats SMALLINT UNSIGNED NOT NULL,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    stats_date DATE NOT NULL,
                                    FOREIGN KEY (user_character_id) REFERENCES user_character(id) ON DELETE CASCADE,
                                    UNIQUE KEY unique_date_user (user_character_id, stats_date)
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


-- 유저 생성시 각 운동별 랭크 스코어 초기화
DELIMITER //

CREATE TRIGGER create_user_rank_scores
    AFTER INSERT ON user_character
    FOR EACH ROW
BEGIN
    INSERT INTO user_rank_scores (user_id, exercise_id, rank_score)
    SELECT NEW.user_id, id, 1000  -- 기본 랭크 점수
    FROM exercise_stats_ratio;  -- 등록된 모든 운동 종류를 가져와 삽입
END //

DELIMITER ;


-- 유저 캐릭터 생성시 튜토리얼에 대한 정보도 자동으로 생성(기본적으로 false)
DELIMITER //

CREATE TRIGGER create_user_tutorial_progress
    AFTER INSERT ON user_character
    FOR EACH ROW
BEGIN
    -- tutorial_types 테이블의 모든 튜토리얼을 가져와서 user_tutorial_progress에 추가
    INSERT INTO user_tutorial_progress (user_id, tutorial_id, is_completed, completed_at)
    SELECT NEW.user_id, id, FALSE, NULL FROM tutorial_types;
END //

DELIMITER ;


-- 유저 캐릭터 생성시 자동으로 튜토리얼 퀘스트 생성
DELIMITER //

CREATE TRIGGER create_squat_quest
AFTER INSERT ON user_character
FOR EACH ROW
BEGIN
    -- 스쿼트 운동 ID 확인 (exercise_stats_ratio에서 ID가 2라고 가정)
    DECLARE squat_exercise_id BIGINT;
    SET squat_exercise_id = (SELECT id FROM exercise_stats_ratio WHERE exercise_type = 'squat' LIMIT 1);

    -- 퀘스트 생성
    INSERT INTO quests (
        user_character_id,
        quest_date,
        exercise_id,
        exercise_cnt,
        real_cnt,
        is_completed,
        message
    ) VALUES (
        NEW.id, -- 새로 추가된 캐릭터의 ID
        CURRENT_DATE, -- 오늘 날짜
        squat_exercise_id, -- 스쿼트 운동 ID
        3, -- 기본 횟수 3회
        0, -- 초기 운동 횟수
        FALSE, -- 달성 여부 기본값
        '스쿼트를 3번 해주세요!'
    );
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER after_exercise_log_insert
    AFTER INSERT ON exercise_log
    FOR EACH ROW
BEGIN
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
END //
DELIMITER ;

-- 스탯 히스토리 업데이트 이후 히스토리 갱신
DELIMITER //
CREATE TRIGGER after_user_stats_update
    AFTER UPDATE ON user_stats
    FOR EACH ROW
BEGIN
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
END //
DELIMITER ;


-- FUNCTION 

DELIMITER //
-- 레벨별 운동 횟수를 계산하는 함수
CREATE FUNCTION calculate_exercise_count(user_level INT)
    RETURNS INT
    DETERMINISTIC
BEGIN
    -- 기본 횟수 5회, 5레벨 단위로 2회씩 증가
RETURN 5 + (2 * FLOOR(user_level / 5));
END //

DELIMITER ;


-- 유저 랭크 업데이트 트리거
DELIMITER //

CREATE TRIGGER update_user_rank_on_update
    AFTER UPDATE ON user_rank_scores
    FOR EACH ROW
BEGIN
    DECLARE avg_rank_score INT;
    DECLARE new_rank VARCHAR(12);

    -- 해당 유저의 운동별 랭크 점수 평균 계산
    SELECT AVG(rank_score) INTO avg_rank_score
    FROM user_rank_scores
    WHERE user_id = NEW.user_id;

    -- 랭크 결정 로직
    IF avg_rank_score >= 1300 THEN
        SET new_rank = 'Diamond';
    ELSEIF avg_rank_score >= 1200 THEN
        SET new_rank = 'Gold';
    ELSEIF avg_rank_score >= 1100 THEN
        SET new_rank = 'Silver';
    ELSE
        SET new_rank = 'Bronze';
END IF;

-- user_character 테이블의 user_rank 업데이트
UPDATE user_character
SET user_rank = new_rank
WHERE user_id = NEW.user_id;
END // 


-- EVENT SCHEDULER

-- 전날 퀘스트 성공 여부를 체크하는 이벤트 스케줄러
DELIMITER //
CREATE EVENT quest_completion_check
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE) + INTERVAL 1 DAY
DO
BEGIN
    -- 전날 퀘스트의 달성 여부 업데이트
UPDATE quests
SET is_completed = CASE
                       WHEN real_cnt >= exercise_cnt THEN TRUE
                       ELSE FALSE
    END
WHERE quest_date = DATE_SUB(TIMESTAMP(CURRENT_DATE), INTERVAL 1 DAY);
END //
DELIMITER ;

-- -- 매일 자정에 새로운 퀘스트를 생성하는 이벤트 스케줄러
-- DELIMITER //
-- CREATE EVENT daily_quest_creation
-- ON SCHEDULE EVERY 1 DAY
-- STARTS CURRENT_DATE + INTERVAL 1 DAY
-- DO
-- BEGIN
--     -- 모든 사용자에 대해 모든 운동 종류의 퀘스트 생성
-- INSERT INTO quests (user_id, quest_date, exercise_id, exercise_cnt, real_cnt, message)
-- SELECT
--     uc.user_id,
--     CURRENT_DATE,
--     esr.id,
--     calculate_exercise_count(uc.user_level),
--     0,  -- real_cnt 초기값
--     CONCAT(esr.exercise_type, '을(를) ',
--            calculate_exercise_count(uc.user_level),
--            '번 해주세요!')
-- FROM user_character uc
--          CROSS JOIN exercise_stats_ratio esr;
-- END //
-- DELIMITER ;

-- 스쿼트 퀘스트만 생성하는 이벤트 스케줄러
DELIMITER //
CREATE EVENT daily_quest_creation
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE) + INTERVAL 1 DAY
DO
BEGIN
    -- 모든 사용자에 대해 스쿼트 퀘스트만 생성
INSERT INTO quests (user_character_id, quest_date, exercise_id, exercise_cnt, real_cnt, message)
SELECT
    uc.id,
    TIMESTAMP(CURRENT_DATE),
    2,  -- 스쿼트의 exercise_id
    calculate_exercise_count(uc.user_level),
    0,  -- real_cnt 초기값
    CONCAT('스쿼트를 ',
    calculate_exercise_count(uc.user_level),
    '번 해주세요!')
FROM user_character uc;
END //
DELIMITER ;

-- 이벤트 스케줄러 활성화 
SET GLOBAL event_scheduler = ON;

-- -- -- -- -- 데이터 삽입 -- -- -- -- --

-- 운동 종류별 점수 비율 데이터
INSERT INTO exercise_stats_ratio (exercise_type, chest_ratio, back_ratio, stamina_ratio, arms_ratio, legs_ratio, abs_ratio)
VALUES
    ('pushup', 0.40, 0.05, 0.20, 0.30, 0.05, 0.05),
    ('squat', 0.05, 0.05, 0.20, 0.05, 0.50, 0.30),
    ('lunge', 0.05, 0.05, 0.15, 0.05, 0.50, 0.30),
    ('plank', 0.05, 0.05, 0.45, 0.05, 0.20, 0.45);


-- 기본 튜토리얼 데이터 삽입
INSERT INTO tutorial_types (id, tutorial_name) VALUES
                                                   (1, 'pushup'),
                                                   (2, 'squat'),
                                                   (3, 'lunge'),
                                                   (4, 'plank'),
                                                   (97, 'Start'),
                                                   (98, 'MainPage'),
                                                   (99, 'UI');

-- user_info 더미 데이터 (20명, 모든 비밀번호는 '1234')
INSERT INTO user_info (user_id, user_password, user_name, user_email, phone_number) VALUES
                                                                                        ('ssafy123', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Hong Gil-dong', 'hong123@gmail.com', '010-1234-5678'),
                                                                                        ('kim456', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Kim Chul-soo', 'kim456@naver.com', '010-2345-6789'),
                                                                                        ('lee789', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Lee Young-hee', 'lee789@daum.net', '010-3456-7890'),
                                                                                        ('park234', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Park Ji-sung', 'park234@gmail.com', '010-4567-8901'),
                                                                                        ('choi567', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Choi Min-soo', 'choi567@naver.com', '010-5678-9012'),
                                                                                        ('jung111', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Jung So-hee', 'jung111@gmail.com', '010-6789-0123'),
                                                                                        ('kang222', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Kang Dong-won', 'kang222@naver.com', '010-7890-1234'),
                                                                                        ('yoon333', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Yoon Seo-jun', 'yoon333@daum.net', '010-8901-2345'),
                                                                                        ('shin444', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Shin Min-ah', 'shin444@gmail.com', '010-9012-3456'),
                                                                                        ('song555', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Song Hye-kyo', 'song555@naver.com', '010-0123-4567'),
                                                                                        ('yang666', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Yang Hyun-suk', 'yang666@gmail.com', '010-1111-2222'),
                                                                                        ('han777', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Han Ji-min', 'han777@naver.com', '010-2222-3333'),
                                                                                        ('oh888', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Oh Yeon-seo', 'oh888@daum.net', '010-3333-4444'),
                                                                                        ('seo999', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Seo In-guk', 'seo999@gmail.com', '010-4444-5555'),
                                                                                        ('bae000', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Bae Suzy', 'bae000@naver.com', '010-5555-6666'),
                                                                                        ('cha123', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Cha Eun-woo', 'cha123@gmail.com', '010-6666-7777'),
                                                                                        ('moon234', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Moon Chae-won', 'moon234@naver.com', '010-7777-8888'),
                                                                                        ('joo345', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Joo Ji-hoon', 'joo345@daum.net', '010-8888-9999'),
                                                                                        ('ryu456', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Ryu Jun-yeol', 'ryu456@gmail.com', '010-9999-0000'),
                                                                                        ('kwon567', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', 'Kwon Sang-woo', 'kwon567@naver.com', '010-0000-1111');


-- 유저 캐릭터 정보
INSERT INTO user_character (user_id, user_nickname, gender, avatar, user_level, user_experience, user_rank, points) VALUES
                                                                                                                        ('ssafy123', 'GilDong', 'M', '7-3-8', 5, 138, 'Silver', 1000),
                                                                                                                        ('kim456', 'ChulSoo123', 'M', '2-6-4', 3, 67, 'Diamond', 500),
                                                                                                                        ('lee789', 'YoungHee', 'F', '1-8-8', 7, 184, 'Diamond', 1500),
                                                                                                                        ('park234', 'JiSungSoccer', 'M', '5-9-2', 4, 76, 'Gold', 800),
                                                                                                                        ('choi567', 'MinSooKing', 'M', '4-7-1', 2, 98, 'Diamond', 300),
                                                                                                                        ('jung111', 'SoHeeJjang', 'F', '10-3-5', 8, 47, 'Diamond', 2000),
                                                                                                                        ('kang222', 'DongWonOppa', 'M', '6-2-8', 6, 182, 'Diamond', 1200),
                                                                                                                        ('yoon333', 'SeoJunMan', 'M', '9-4-1', 9, 35, 'Diamond', 2300),
                                                                                                                        ('shin444', 'MinAhGirl', 'F', '3-7-6', 3, 144, 'Diamond', 600),
                                                                                                                        ('song555', 'HyeKyoNim', 'F', '8-5-2', 5, 61, 'Bronze', 1100),
                                                                                                                        ('yang666', 'HyunSeokBoss', 'M', '7-4-8', 6, 95, 'Gold', 1700),
                                                                                                                        ('han777', 'JiMinLove', 'F', '5-3-2', 4, 135, 'Diamond', 900),
                                                                                                                        ('oh888', 'YeonSeoStar', 'F', '2-9-6', 7, 150, 'Gold', 1600),
                                                                                                                        ('seo999', 'InGukRocks', 'M', '1-6-4', 5, 122, 'Silver', 1200),
                                                                                                                        ('bae000', 'SuzyBae', 'F', '9-8-5', 3, 103, 'Silver', 700),
                                                                                                                        ('cha123', 'EunWooOppa', 'M', '6-7-3', 6, 131, 'Gold', 1300),
                                                                                                                        ('moon234', 'ChaeWonMoon', 'F', '4-5-7', 5, 85, 'Bronze', 1100),
                                                                                                                        ('joo345', 'JiHoonJjang', 'M', '3-9-2', 7, 155, 'Gold', 1800),
                                                                                                                        ('ryu456', 'JunYeolBest', 'M', '8-2-4', 4, 101, 'Diamond', 900),
                                                                                                                        ('kwon567', 'SangWooPower', 'M', '7-6-1', 5, 139, 'Gold', 1400);



TRUNCATE TABLE user_rank_scores;

INSERT INTO user_rank_scores (user_id, exercise_id, rank_score) VALUES
                                                                    ('ssafy123', 1, 1148),
                                                                    ('ssafy123', 2, 1238),
                                                                    ('ssafy123', 3, 1080),
                                                                    ('ssafy123', 4, 1228),
                                                                    ('kim456', 1, 1706),
                                                                    ('kim456', 2, 1109),
                                                                    ('kim456', 3, 1347),
                                                                    ('kim456', 4, 1403),
                                                                    ('lee789', 1, 1020),
                                                                    ('lee789', 2, 1237),
                                                                    ('lee789', 3, 1789),
                                                                    ('lee789', 4, 1587),
                                                                    ('park234', 1, 866),
                                                                    ('park234', 2, 1728),
                                                                    ('park234', 3, 991),
                                                                    ('park234', 4, 1349),
                                                                    ('choi567', 1, 1177),
                                                                    ('choi567', 2, 1575),
                                                                    ('choi567', 3, 1633),
                                                                    ('choi567', 4, 1360),
                                                                    ('jung111', 1, 1782),
                                                                    ('jung111', 2, 1631),
                                                                    ('jung111', 3, 857),
                                                                    ('jung111', 4, 1217),
                                                                    ('kang222', 1, 1632),
                                                                    ('kang222', 2, 1250),
                                                                    ('kang222', 3, 940),
                                                                    ('kang222', 4, 1608),
                                                                    ('yoon333', 1, 1506),
                                                                    ('yoon333', 2, 1678),
                                                                    ('yoon333', 3, 1028),
                                                                    ('yoon333', 4, 1698),
                                                                    ('shin444', 1, 1301),
                                                                    ('shin444', 2, 1460),
                                                                    ('shin444', 3, 1399),
                                                                    ('shin444', 4, 1328),
                                                                    ('song555', 1, 912),
                                                                    ('song555', 2, 905),
                                                                    ('song555', 3, 938),
                                                                    ('song555', 4, 1047),
                                                                    ('yang666', 1, 869),
                                                                    ('yang666', 2, 1553),
                                                                    ('yang666', 3, 1699),
                                                                    ('yang666', 4, 887),
                                                                    ('han777', 1, 1132),
                                                                    ('han777', 2, 1468),
                                                                    ('han777', 3, 1470),
                                                                    ('han777', 4, 1455),
                                                                    ('oh888', 1, 862),
                                                                    ('oh888', 2, 1679),
                                                                    ('oh888', 3, 911),
                                                                    ('oh888', 4, 1476),
                                                                    ('seo999', 1, 1454),
                                                                    ('seo999', 2, 1186),
                                                                    ('seo999', 3, 892),
                                                                    ('seo999', 4, 912),
                                                                    ('bae000', 1, 954),
                                                                    ('bae000', 2, 1364),
                                                                    ('bae000', 3, 1355),
                                                                    ('bae000', 4, 878),
                                                                    ('cha123', 1, 1073),
                                                                    ('cha123', 2, 1212),
                                                                    ('cha123', 3, 1729),
                                                                    ('cha123', 4, 930),
                                                                    ('moon234', 1, 1068),
                                                                    ('moon234', 2, 819),
                                                                    ('moon234', 3, 1076),
                                                                    ('moon234', 4, 1085),
                                                                    ('joo345', 1, 1479),
                                                                    ('joo345', 2, 993),
                                                                    ('joo345', 3, 895),
                                                                    ('joo345', 4, 1619),
                                                                    ('ryu456', 1, 1590),
                                                                    ('ryu456', 2, 886),
                                                                    ('ryu456', 3, 1346),
                                                                    ('ryu456', 4, 1492),
                                                                    ('kwon567', 1, 1309),
                                                                    ('kwon567', 2, 1480),
                                                                    ('kwon567', 3, 1098),
                                                                    ('kwon567', 4, 1303);

UPDATE user_stats
SET
    arms_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    legs_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    chest_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    abs_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    back_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    stamina_stats = FLOOR(10 + (RAND() * (1000 - 10))),
    updated_at = DATE_ADD(NOW(), INTERVAL -FLOOR(RAND() * 365) DAY)
WHERE id > 0;

INSERT INTO quests (user_character_id, quest_date, quest_time, exercise_id, exercise_cnt, real_cnt, is_completed, message)
VALUES
    ('1', current_date(), NULL, '2', '7', '7', '1', '스쿼트를 7번 해주세요!'),
    ('2', current_date(), '00:00:53', '2', '7', '2', '0', '스쿼트를 7번 해주세요!');


-- 게임 전적 정보 입력
INSERT INTO game_results (game_id, duration, exercise_id, user_id, opponent_id, user_score, opponent_score, result) VALUES
                                                                                                                        ('game_001', 60, 2, 'hong123', 'kim456', 10, 8, 'WIN'),
                                                                                                                        ('game_001', 60, 2, 'kim456', 'hong123', 8, 10, 'LOSE'),
                                                                                                                        ('game_002', 60, 2, 'lee789', 'park234', 12, 12, 'DRAW'),
                                                                                                                        ('game_002', 60, 2, 'park234', 'lee789', 12, 12, 'DRAW'),
                                                                                                                        ('game_003', 60, 2, 'choi567', 'jung111', 5, 9, 'LOSE'),
                                                                                                                        ('game_003', 60, 2, 'jung111', 'choi567', 9, 5, 'WIN'),
                                                                                                                        ('game_004', 60, 2, 'kang222', 'yoon333', 15, 18, 'LOSE'),
                                                                                                                        ('game_004', 60, 2, 'yoon333', 'kang222', 18, 15, 'WIN'),
                                                                                                                        ('game_005', 60, 2, 'shin444', 'song555', 7, 7, 'DRAW'),
                                                                                                                        ('game_005', 60, 2, 'song555', 'shin444', 7, 7, 'DRAW'),
                                                                                                                        ('game_006', 60, 2, 'yang666', 'han777', 13, 10, 'WIN'),
                                                                                                                        ('game_006', 60, 2, 'han777', 'yang666', 10, 13, 'LOSE'),
                                                                                                                        ('game_007', 60, 2, 'oh888', 'seo999', 6, 14, 'LOSE'),
                                                                                                                        ('game_007', 60, 2, 'seo999', 'oh888', 14, 6, 'WIN'),
                                                                                                                        ('game_008', 60, 2, 'bae000', 'cha123', 20, 19, 'WIN'),
                                                                                                                        ('game_008', 60, 2, 'cha123', 'bae000', 19, 20, 'LOSE'),
                                                                                                                        ('game_009', 60, 2, 'moon234', 'joo345', 9, 11, 'LOSE'),
                                                                                                                        ('game_009', 60, 2, 'joo345', 'moon234', 11, 9, 'WIN'),
                                                                                                                        ('game_010', 60, 2, 'ryu456', 'kwon567', 15, 15, 'DRAW'),
                                                                                                                        ('game_010', 60, 2, 'kwon567', 'ryu456', 15, 15, 'DRAW');

INSERT INTO exercise_log (user_id, exercise_duration, exercise_cnt, exercise_stats_ratio_id, exercise_date)
SELECT
    user_id,
    FLOOR(10 + (RAND() * (180 - 10))) AS exercise_duration,
    FLOOR(1 + (RAND() * (5 - 1))) AS exercise_cnt,
    2 AS exercise_stats_ratio_id,  -- 스쿼트만 지정
    DATE_ADD(NOW(), INTERVAL -FLOOR(RAND() * 365) DAY) AS exercise_date
FROM (
         SELECT 'ssafy123' AS user_id UNION ALL
         SELECT 'kim456' UNION ALL
         SELECT 'lee789' UNION ALL
         SELECT 'park234' UNION ALL
         SELECT 'choi567' UNION ALL
         SELECT 'jung111' UNION ALL
         SELECT 'kang222' UNION ALL
         SELECT 'yoon333' UNION ALL
         SELECT 'shin444' UNION ALL
         SELECT 'song555' UNION ALL
         SELECT 'yang666' UNION ALL
         SELECT 'han777' UNION ALL
         SELECT 'oh888' UNION ALL
         SELECT 'seo999' UNION ALL
         SELECT 'bae000' UNION ALL
         SELECT 'cha123' UNION ALL
         SELECT 'moon234' UNION ALL
         SELECT 'joo345' UNION ALL
         SELECT 'ryu456' UNION ALL
         SELECT 'kwon567'
     ) AS users
ORDER BY RAND()
    LIMIT 50;

INSERT INTO quests (user_character_id, quest_date, quest_time, exercise_id, exercise_cnt, real_cnt, is_completed, message)
VALUES
    (1, '2025-02-20', NULL, '2', 7, 0, 0, '스쿼트를 7번 해주세요!'),
    (2, '2025-02-20', '00:04:32', '2', 7, 2, 0, '스쿼트를 7번 해주세요!'),
    (3, '2025-02-20', '00:02:15', '2', 9, 5, 0, '스쿼트를 9번 해주세요!'),
    (4, '2025-02-20', NULL, '2', 5, 0, 0, '스쿼트를 5번 해주세요!'),
    (5, '2025-02-20', '00:05:21', '2', 5, 3, 0, '스쿼트를 5번 해주세요!'),
    (6, '2025-02-20', NULL, '2', 7, 0, 0, '스쿼트를 7번 해주세요!'),
    (7, '2025-02-20', '00:03:48', '2', 9, 6, 0, '스쿼트를 9번 해주세요!'),
    (8, '2025-02-20', NULL, '2', 5, 0, 0, '스쿼트를 5번 해주세요!'),
    (9, '2025-02-20', '00:01:55', '2', 7, 4, 0, '스쿼트를 7번 해주세요!'),
    (10, '2025-02-20', '00:05:10', '2', 5, 2, 0, '스쿼트를 5번 해주세요!'),
    (11, '2025-02-20', NULL, '2', 7, 0, 0, '스쿼트를 7번 해주세요!'),
    (12, '2025-02-20', '00:03:22', '2', 9, 7, 0, '스쿼트를 9번 해주세요!'),
    (13, '2025-02-20', NULL, '2', 5, 0, 0, '스쿼트를 5번 해주세요!'),
    (14, '2025-02-20', '00:02:50', '2', 7, 3, 0, '스쿼트를 7번 해주세요!'),
    (15, '2025-02-20', '00:04:44', '2', 9, 5, 0, '스쿼트를 9번 해주세요!'),
    (16, '2025-02-20', NULL, '2', 5, 0, 0, '스쿼트를 5번 해주세요!'),
    (17, '2025-02-20', '00:03:10', '2', 7, 4, 0, '스쿼트를 7번 해주세요!'),
    (18, '2025-02-20', NULL, '2', 5, 0, 0, '스쿼트를 5번 해주세요!'),
    (19, '2025-02-20', '00:01:40', '2', 9, 6, 0, '스쿼트를 9번 해주세요!'),
    (20, '2025-02-20', '00:02:59', '2', 7, 2, 0, '스쿼트를 7번 해주세요!');


-- 시연용 캐릭터 더미데이터 추가
-- 1. game_results 데이터 추가 (ssafy123의 다른 사용자들과의 게임 결과)
INSERT INTO game_results (game_id, exercise_id, user_id, opponent_id, user_score, opponent_score, result, created_at)
VALUES
-- 1월 게임 결과
('game-20250105-001', 2, 'ssafy123', 'kim456', 25, 20, 'WIN', '2025-01-05 14:30:00'),
('game-20250108-002', 2, 'ssafy123', 'lee789', 18, 22, 'LOSE', '2025-01-08 18:15:00'),
('game-20250112-003', 2, 'ssafy123', 'park234', 30, 25, 'WIN', '2025-01-12 10:45:00'),
('game-20250115-004', 2, 'ssafy123', 'choi567', 22, 22, 'DRAW', '2025-01-15 20:30:00'),
('game-20250120-005', 2, 'ssafy123', 'jung111', 15, 28, 'LOSE', '2025-01-20 16:00:00'),
('game-20250123-006', 2, 'ssafy123', 'kang222', 27, 25, 'WIN', '2025-01-23 19:10:00'),
('game-20250127-007', 2, 'ssafy123', 'yoon333', 19, 24, 'LOSE', '2025-01-27 15:45:00'),
('game-20250130-008', 2, 'ssafy123', 'shin444', 26, 18, 'WIN', '2025-01-30 12:20:00'),
-- 2월 게임 결과
('game-20250202-009', 2, 'ssafy123', 'song555', 30, 17, 'WIN', '2025-02-02 11:00:00'),
('game-20250205-010', 2, 'ssafy123', 'yang666', 20, 24, 'LOSE', '2025-02-05 17:30:00'),
('game-20250208-011', 2, 'ssafy123', 'han777', 25, 25, 'DRAW', '2025-02-08 13:45:00'),
('game-20250210-012', 2, 'ssafy123', 'oh888', 28, 22, 'WIN', '2025-02-10 18:30:00'),
('game-20250212-013', 2, 'ssafy123', 'seo999', 19, 26, 'LOSE', '2025-02-12 14:15:00'),
('game-20250215-014', 2, 'ssafy123', 'bae000', 32, 27, 'WIN', '2025-02-15 20:00:00'),
('game-20250218-015', 2, 'ssafy123', 'cha123', 24, 29, 'LOSE', '2025-02-18 16:45:00');

-- 2. exercise_log 데이터 추가 (ssafy123의 운동 기록)
INSERT INTO exercise_log (user_id, exercise_duration, exercise_cnt, exercise_stats_ratio_id, exercise_date)
VALUES
-- 1월 운동 기록
('ssafy123', 60, 15, 2, '2025-01-03 08:30:00'),
('ssafy123', 120, 25, 2, '2025-01-05 09:15:00'),
('ssafy123', 180, 35, 2, '2025-01-05 18:45:00'),
('ssafy123', 60, 18, 2, '2025-01-08 07:30:00'),
('ssafy123', 90, 20, 2, '2025-01-10 19:00:00'),
('ssafy123', 60, 17, 2, '2025-01-12 10:30:00'),
('ssafy123', 120, 30, 2, '2025-01-12 16:45:00'),
('ssafy123', 60, 15, 2, '2025-01-15 08:00:00'),
('ssafy123', 180, 40, 2, '2025-01-17 17:30:00'),
('ssafy123', 90, 22, 2, '2025-01-20 06:45:00'),
('ssafy123', 120, 28, 2, '2025-01-22 18:15:00'),
('ssafy123', 60, 16, 2, '2025-01-25 07:30:00'),
('ssafy123', 120, 30, 2, '2025-01-27 16:00:00'),
('ssafy123', 180, 45, 2, '2025-01-30 18:45:00'),
-- 2월 운동 기록
('ssafy123', 60, 18, 2, '2025-02-01 09:00:00'),
('ssafy123', 90, 22, 2, '2025-02-02 17:30:00'),
('ssafy123', 120, 32, 2, '2025-02-04 07:45:00'),
('ssafy123', 60, 16, 2, '2025-02-05 18:30:00'),
('ssafy123', 180, 40, 2, '2025-02-08 10:15:00'),
('ssafy123', 90, 25, 2, '2025-02-10 06:45:00'),
('ssafy123', 120, 35, 2, '2025-02-10 19:00:00'),
('ssafy123', 60, 19, 2, '2025-02-12 08:15:00'),
('ssafy123', 180, 45, 2, '2025-02-15 16:30:00'),
('ssafy123', 90, 24, 2, '2025-02-17 07:00:00'),
('ssafy123', 120, 30, 2, '2025-02-18 19:45:00');

-- 3. quests 데이터 추가 (ssafy123의 퀘스트 기록)
INSERT INTO quests (user_character_id, quest_date, quest_time, exercise_id, exercise_cnt, real_cnt, is_completed, message)
VALUES
-- 1월 퀘스트 (ssafy123의 캐릭터 ID는 1이고, 레벨 5에 해당하는 exercise_cnt는 7)
('1', '2025-01-03', '08:30:00', '2', '7', '15', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-04', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-05', '18:45:00', '2', '7', '60', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-06', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-07', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-08', '07:30:00', '2', '7', '18', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-09', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-10', '19:00:00', '2', '7', '20', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-11', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-12', '16:45:00', '2', '7', '47', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-13', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-14', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-15', '08:00:00', '2', '7', '15', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-16', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-17', '17:30:00', '2', '7', '40', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-18', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-19', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-20', '06:45:00', '2', '7', '22', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-21', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-22', '18:15:00', '2', '7', '28', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-23', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-24', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-25', '07:30:00', '2', '7', '16', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-26', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-27', '16:00:00', '2', '7', '30', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-28', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-29', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-01-30', '18:45:00', '2', '7', '45', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-01-31', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
-- 2월 퀘스트
('1', '2025-02-01', '09:00:00', '2', '7', '18', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-02', '17:30:00', '2', '7', '22', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-03', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-04', '07:45:00', '2', '7', '32', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-05', '18:30:00', '2', '7', '16', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-06', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-07', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-08', '10:15:00', '2', '7', '40', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-09', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-10', '19:00:00', '2', '7', '60', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-11', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-12', '08:15:00', '2', '7', '19', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-13', NULL, '2', '7', '4', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-14', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-15', '16:30:00', '2', '7', '45', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-16', NULL, '2', '7', '0', '0', '스쿼트를 7번 해주세요!'),
('1', '2025-02-17', '07:00:00', '2', '7', '24', '1', '스쿼트를 7번 해주세요!'),
('1', '2025-02-18', '19:45:00', '2', '7', '30', '1', '스쿼트를 7번 해주세요!');

-- 먼저 초기 스탯 값을 설정합니다 (exercise_log 이전의 기준값)
INSERT INTO user_stats_history (user_character_id, arms_stats, legs_stats, chest_stats, abs_stats, back_stats, stamina_stats, updated_at, stats_date)
VALUES 
(1, 200, 250, 220, 180, 190, 210, '2025-01-02 23:59:59', '2025-01-02');

-- 4. user_stats_history 데이터 추가 (ssafy123의 스탯 히스토리)
-- 운동 기록에 따라 스탯이 증가하는 패턴으로 데이터 생성
INSERT INTO user_stats_history (user_character_id, arms_stats, legs_stats, chest_stats, abs_stats, back_stats, stamina_stats, updated_at, stats_date)
VALUES
-- 1월 스탯 히스토리
-- 1월 3일: 15회 스쿼트 (legs +7.5, abs +4.5, stamina +3, arms/chest/back +0.75 each)
(1, 200.75, 257.5, 220.75, 184.5, 190.75, 213, '2025-01-03 20:00:00', '2025-01-03'),
-- 1월 5일: 60회 스쿼트 (legs +30, abs +18, stamina +12, arms/chest/back +3 each)
(1, 203.75, 287.5, 223.75, 202.5, 193.75, 225, '2025-01-05 20:00:00', '2025-01-05'),
-- 1월 8일: 18회 스쿼트 (legs +9, abs +5.4, stamina +3.6, arms/chest/back +0.9 each)
(1, 204.65, 296.5, 224.65, 207.9, 194.65, 228.6, '2025-01-08 20:00:00', '2025-01-08'),
-- 1월 10일: 20회 스쿼트 (legs +10, abs +6, stamina +4, arms/chest/back +1 each)
(1, 205.65, 306.5, 225.65, 213.9, 195.65, 232.6, '2025-01-10 20:00:00', '2025-01-10'),
-- 1월 12일: 47회 스쿼트 (legs +23.5, abs +14.1, stamina +9.4, arms/chest/back +2.35 each)
(1, 208, 330, 228, 228, 198, 242, '2025-01-12 20:00:00', '2025-01-12'),
-- 1월 15일: 15회 스쿼트 (legs +7.5, abs +4.5, stamina +3, arms/chest/back +0.75 each)
(1, 208.75, 337.5, 228.75, 232.5, 198.75, 245, '2025-01-15 20:00:00', '2025-01-15'),
-- 1월 17일: 40회 스쿼트 (legs +20, abs +12, stamina +8, arms/chest/back +2 each)
(1, 210.75, 357.5, 230.75, 244.5, 200.75, 253, '2025-01-17 20:00:00', '2025-01-17'),
-- 1월 20일: 22회 스쿼트 (legs +11, abs +6.6, stamina +4.4, arms/chest/back +1.1 each)
(1, 211.85, 368.5, 231.85, 251.1, 201.85, 257.4, '2025-01-20 20:00:00', '2025-01-20'),
-- 1월 22일: 28회 스쿼트 (legs +14, abs +8.4, stamina +5.6, arms/chest/back +1.4 each)
(1, 213.25, 382.5, 233.25, 259.5, 203.25, 263, '2025-01-22 20:00:00', '2025-01-22'),
-- 1월 25일: 16회 스쿼트 (legs +8, abs +4.8, stamina +3.2, arms/chest/back +0.8 each)
(1, 214.05, 390.5, 234.05, 264.3, 204.05, 266.2, '2025-01-25 20:00:00', '2025-01-25'),
-- 1월 27일: 30회 스쿼트 (legs +15, abs +9, stamina +6, arms/chest/back +1.5 each)
(1, 215.55, 405.5, 235.55, 273.3, 205.55, 272.2, '2025-01-27 20:00:00', '2025-01-27'),
-- 1월 30일: 45회 스쿼트 (legs +22.5, abs +13.5, stamina +9, arms/chest/back +2.25 each)
(1, 217.8, 428, 237.8, 286.8, 207.8, 281.2, '2025-01-30 20:00:00', '2025-01-30'),
-- 2월 스탯 히스토리
-- 2월 1일: 18회 스쿼트 (legs +9, abs +5.4, stamina +3.6, arms/chest/back +0.9 each)
(1, 218.7, 437, 238.7, 292.2, 208.7, 284.8, '2025-02-01 20:00:00', '2025-02-01'),
-- 2월 2일: 22회 스쿼트 (legs +11, abs +6.6, stamina +4.4, arms/chest/back +1.1 each)
(1, 219.8, 448, 239.8, 298.8, 209.8, 289.2, '2025-02-02 20:00:00', '2025-02-02'),
-- 2월 4일: 32회 스쿼트 (legs +16, abs +9.6, stamina +6.4, arms/chest/back +1.6 each)
(1, 221.4, 464, 241.4, 308.4, 211.4, 295.6, '2025-02-04 20:00:00', '2025-02-04'),
-- 2월 5일: 16회 스쿼트 (legs +8, abs +4.8, stamina +3.2, arms/chest/back +0.8 each)
(1, 222.2, 472, 242.2, 313.2, 212.2, 298.8, '2025-02-05 20:00:00', '2025-02-05'),
-- 2월 8일: 40회 스쿼트 (legs +20, abs +12, stamina +8, arms/chest/back +2 each)
(1, 224.2, 492, 244.2, 325.2, 214.2, 306.8, '2025-02-08 20:00:00', '2025-02-08'),
-- 2월 10일: 60회 스쿼트 (legs +30, abs +18, stamina +12, arms/chest/back +3 each)
(1, 227.2, 522, 247.2, 343.2, 217.2, 318.8, '2025-02-10 20:00:00', '2025-02-10'),
-- 2월 12일: 19회 스쿼트 (legs +9.5, abs +5.7, stamina +3.8, arms/chest/back +0.95 each)
(1, 228.15, 531.5, 248.15, 348.9, 218.15, 322.6, '2025-02-12 20:00:00', '2025-02-12'),
-- 2월 15일: 45회 스쿼트 (legs +22.5, abs +13.5, stamina +9, arms/chest/back +2.25 each)
(1, 230.4, 554, 250.4, 362.4, 220.4, 331.6, '2025-02-15 20:00:00', '2025-02-15'),
-- 2월 17일: 24회 스쿼트 (legs +12, abs +7.2, stamina +4.8, arms/chest/back +1.2 each)
(1, 231.6, 566, 251.6, 369.6, 221.6, 336.4, '2025-02-17 20:00:00', '2025-02-17'),
-- 2월 18일: 30회 스쿼트 (legs +15, abs +9, stamina +6, arms/chest/back +1.5 each)
(1, 233.1, 581, 253.1, 378.6, 223.1, 342.4, '2025-02-18 20:00:00', '2025-02-18');