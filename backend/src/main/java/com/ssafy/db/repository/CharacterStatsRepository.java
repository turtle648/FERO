package com.ssafy.db.repository;

import com.ssafy.db.entity.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 유저 스탯 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface CharacterStatsRepository extends JpaRepository<UserStats, Long> {
    // 아래와 같이, Query Method 인터페이스(반환값, 메소드명, 인자) 정의를 하면 자동으로 Query Method 구현됨.
    Optional<User> findByUserId(String userId);
    // user_name 과 user_email 이 일치하는 회원 조회
    Optional<User> findByUserNameAndUserEmail(String userName, String userEmail);
    // user_email 과 user_id 이 일치하는 회원 조회
    Optional<User> findByUserEmailAndUserId(String userEmail, String userId);
    // user_email 중복 확인
    boolean existsByUserEmail(String userEmail);
}
