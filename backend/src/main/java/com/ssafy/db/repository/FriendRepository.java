package com.ssafy.db.repository;

import com.ssafy.db.entity.Friend;
import com.ssafy.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 특정 유저의 친구 목록 가져오기
    List<Friend> findByUserIdAndStatus(String userId, Friend.FriendStatus status);

//    Optional<Object> findByUserAndFriend(User user, User friend);

//    // 친구 요청이 이미 존재하는지 확인
    Optional<Friend> findByUserIdAndFriendId(String userId, String friendId);

//    List<Friend> findByUserIdAndStatus(Friend.FriendStatus status);

//    // 친구 요청 삭제 (친구 관계 끊기)
//    void deleteByUserAndFriend(User user, User friend);
}
