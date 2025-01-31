package com.ssafy.db.repository;

import com.ssafy.db.entity.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


    @Repository
    public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

        @Query("SELECT c FROM ChatRoom c WHERE " +
                "(c.userId1 = :user1Id AND c.userId2 = :user2Id) OR " +
                "(c.userId1 = :user2Id AND c.userId2 = :user1Id)")
        Optional<ChatRoom> findByUsers(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);
    }
