package com.ssafy.api.service;

import com.ssafy.api.response.FriendRes;
import com.ssafy.db.entity.Friend;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.FriendRepository;
import com.ssafy.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

//    // 친구 요청 보내기
    @Transactional
    public void sendFriendRequest(String userId, String friendId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        User friend = userRepository.findByUserId(friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 대상 유저 없음"));

        // 이미 친구 요청이 있는지 확인
        if (friendRepository.findByUserAndFriend(user, friend).isPresent()) {
            throw new IllegalStateException("이미 친구 요청을 보냈거나 친구 상태입니다.");
        }

        Friend newFriendRequest = Friend.builder()
                .user(user)
                .friend(friend)
                .friendNickname(friend.getUserCharacter().getUserNickname())
                .friendLevel(friend.getUserCharacter().getUserLevel())
                .status(Friend.FriendStatus.pending)
                .build();

        friendRepository.save(newFriendRequest);
    }

//    // 친구 요청 수락
//    @Transactional
//    public void acceptFriendRequest(String userId, String friendId) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
//        User friend = userRepository.findByUserId(friendId)
//                .orElseThrow(() -> new IllegalArgumentException("친구 대상 유저 없음"));
//
//        Friend friendRequest = friendRepository.findByUserAndFriend(friend, user)
//                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 없습니다."));
//
//        friendRequest.setStatus(Friend.FriendStatus.ACCEPTED);
//        friendRepository.save(friendRequest);
//
//        // 반대 방향의 친구 관계도 저장 (서로 친구)
//        Friend reverseFriend = Friend.builder()
//                .user(user)
//                .friend(friend)
//                .friendNickname(friend.getNickname())
//                .friendLevel(friend.getLevel())
//                .status(Friend.FriendStatus.ACCEPTED)
//                .build();
//
//        friendRepository.save(reverseFriend);
//    }
//
//    // 친구 삭제
//    @Transactional
//    public void removeFriend(String userId, String friendId) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
//        User friend = userRepository.findByUserId(friendId)
//                .orElseThrow(() -> new IllegalArgumentException("친구 대상 유저 없음"));
//
//        friendRepository.deleteByUserAndFriend(user, friend);
//        friendRepository.deleteByUserAndFriend(friend, user); // 반대 방향 삭제
//    }

    // 친구 목록 조회
    public List<FriendRes> getFriends(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<Friend> friends = friendRepository.findByUserAndStatus(user, Friend.FriendStatus.accepted);

        return friends.stream().map(friend -> new FriendRes(
                friend.getFriend().getUserId(),
                friend.getFriendNickname(),
                friend.getFriendLevel(),
                friend.getStatus()
        )).collect(Collectors.toList());
    }
}
