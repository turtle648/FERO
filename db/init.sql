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

                                user_level SMALLINT UNSIGNED NOT NULL DEFAULT 1 CHECK (user_level <= 999),
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
                                               (99, 'UI');

-- user_info 더미 데이터 (20명, 모든 비밀번호는 'moonym1')
INSERT INTO user_info (user_id, user_password, user_name, user_email, phone_number) VALUES
('ssafy123', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '홍길동', 'hong123@gmail.com', '010-1234-5678'),
('kim456', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '김철수', 'kim456@naver.com', '010-2345-6789'),
('lee789', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '이영희', 'lee789@daum.net', '010-3456-7890'),
('park234', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '박지성', 'park234@gmail.com', '010-4567-8901'),
('choi567', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '최민수', 'choi567@naver.com', '010-5678-9012'),
('jung111', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '정소희', 'jung111@gmail.com', '010-6789-0123'),
('kang222', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '강동원', 'kang222@naver.com', '010-7890-1234'),
('yoon333', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '윤서준', 'yoon333@daum.net', '010-8901-2345'),
('shin444', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '신민아', 'shin444@gmail.com', '010-9012-3456'),
('song555', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '송혜교', 'song555@naver.com', '010-0123-4567'),
('yang666', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '양현석', 'yang666@gmail.com', '010-1111-2222'),
('han777', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '한지민', 'han777@naver.com', '010-2222-3333'),
('oh888', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '오연서', 'oh888@daum.net', '010-3333-4444'),
('seo999', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '서인국', 'seo999@gmail.com', '010-4444-5555'),
('bae000', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '배수지', 'bae000@naver.com', '010-5555-6666'),
('cha123', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '차은우', 'cha123@gmail.com', '010-6666-7777'),
('moon234', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '문채원', 'moon234@naver.com', '010-7777-8888'),
('joo345', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '주지훈', 'joo345@daum.net', '010-8888-9999'),
('ryu456', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '류준열', 'ryu456@gmail.com', '010-9999-0000'),
('kwon567', '$2a$10$9VI57YSLYInyXsp74P1FaOIvVUzFXbV2BRgi.ar/5bBUrpj7S3Gg.', '권상우', 'kwon567@naver.com', '010-0000-1111');

INSERT INTO user_character (user_id, user_nickname, gender, user_level, user_experience, points) VALUES
('ssafy123', '길동이', 'M', 5, 128, 1000),
('kim456', '철수123', 'M', 3, 57, 500),
('lee789', '영희공주', 'F', 7, 184, 1500),
('park234', '지성축구', 'M', 4, 76, 800),
('choi567', '민수킹', 'M', 2, 93, 300),
('jung111', '소희쨩', 'F', 8, 42, 2000),
('kang222', '동원오빠', 'M', 6, 177, 1200),
('yoon333', '서준맨', 'M', 9, 25, 2300),
('shin444', '민아걸', 'F', 3, 134, 600),
('song555', '혜교님', 'F', 5, 61, 1100),
('yang666', '현석왕', 'M', 4, 150, 900),
('han777', '지민공듀', 'F', 7, 92, 1700),
('oh888', '연서비누', 'F', 2, 189, 400),
('seo999', '인국왕자', 'M', 6, 78, 1400),
('bae000', '수지짱', 'F', 8, 46, 1900),
('cha123', '은우꽃미남', 'M', 5, 173, 1200),
('moon234', '채원공주', 'F', 3, 55, 700),
('joo345', '지훈멋짐', 'M', 9, 99, 2200),
('ryu456', '준열검사', 'M', 4, 37, 1000),
('kwon567', '상우형님', 'M', 7, 35, 1600);


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

INSERT INTO exercise_log (user_id, exercise_duration, exercise_cnt, exercise_stats_ratio_id, exercise_date)
SELECT 
    user_id,
    FLOOR(10 + (RAND() * (180 - 10))) AS exercise_duration,
    FLOOR(1 + (RAND() * (5 - 1))) AS exercise_cnt,
    FLOOR(1 + (RAND() * 4)) AS exercise_stats_ratio_id,
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

UPDATE user_tutorial_progress
SET is_completed = 1, 
    completed_at = NOW()
WHERE id IN (
    SELECT id FROM (
        SELECT id FROM user_tutorial_progress ORDER BY RAND() LIMIT 30
    ) AS subquery
);