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
    public void sendFriendRequest(String user1Id, String user2Id) {
        User user = userRepository.findByUserId(user1Id)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        User friend = userRepository.findByUserId(user2Id)
                .orElseThrow(() -> new IllegalArgumentException("친구 대상 유저 없음"));

        // 이미 친구 요청이 있는지 확인
        if (friendRepository.findByUserIdAndFriendId(user1Id, user2Id).isPresent()) {
            throw new IllegalStateException("이미 친구 요청을 보냈거나 친구 상태입니다.");
        }

        // user1의 친구 목록에 user2 등록
        Friend newFriendRequest = Friend.builder()
                .userId(user1Id)
                .friendId(user2Id)
                .friendNickname(friend.getUserCharacter().getUserNickname())
                .friendLevel(friend.getUserCharacter().getUserLevel())
                .status(Friend.FriendStatus.pending)
                .build();
        friendRepository.save(newFriendRequest);

//        Friend newFriendRequest2 = Friend.builder()
//                .userId(user2Id)
//                .friendId(user1Id)
//                .friendNickname(user.getUserCharacter().getUserNickname())
//                .friendLevel(user.getUserCharacter().getUserLevel())
//                .status(Friend.FriendStatus.pending)
//                .build();
//        friendRepository.save(newFriendRequest2);

    }

//    // 친구 요청 수락
    @Transactional
    public void acceptFriendRequest(String userId, String friendId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        User friend = userRepository.findByUserId(friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 대상 유저 없음"));

        Friend friendRequest = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청이 없습니다."));

        friendRequest.setStatus(Friend.FriendStatus.accepted);
        friendRepository.save(friendRequest);

        // 반대 방향의 친구 관계도 저장 (서로 친구)
        Friend reverseFriend = Friend.builder()
                .userId(friendId)
                .friendId(userId)
                .friendNickname(user.getUserCharacter().getUserNickname())
                .friendLevel(user.getUserCharacter().getUserLevel())
                .status(Friend.FriendStatus.accepted)
                .build();

        friendRepository.save(reverseFriend);
    }
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

        List<Friend> friends = friendRepository.findByUserIdAndStatus(userId, Friend.FriendStatus.accepted);

        return friends.stream().map(friend -> new FriendRes(
                friend.getFriendId(),
                friend.getFriendNickname(),
                friend.getFriendLevel(),
                friend.getStatus()
        )).collect(Collectors.toList());
    }
}
