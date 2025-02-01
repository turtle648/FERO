package com.ssafy.db.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.db.entity.QUser;
import com.ssafy.db.entity.QUserCharacter;
import com.ssafy.db.entity.UserCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCharacterRepositorySupport {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QUserCharacter qUserCharacter = QUserCharacter.userCharacter;
    QUser qUser = QUser.user;

//    public Optional<UserCharacter> findUserCharacterByUserId(String userId) {
//        UserCharacter userCharacter = jpaQueryFactory
//                .selectFrom(qUserCharacter)
//                .where(qUserCharacter.userId.eq(userId))
//                .fetchOne();
//        return Optional.ofNullable(userCharacter);
//    }
}
