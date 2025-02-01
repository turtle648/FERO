package com.ssafy.db.repository;

import com.ssafy.db.entity.UserCharacter;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    @Query("SELECT uc FROM UserCharacter uc JOIN FETCH uc.user u WHERE u.userId = :userId")
    Optional<UserCharacter> findUserCharacterByUserId(@Param("userId") String userId);
}