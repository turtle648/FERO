package com.ssafy.db.repository;

import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    @Transactional
    @Modifying
    @Query("DELETE FROM ChatMessage m WHERE m.sentAt < :date")
    default int deleteBySentAtBefore(@Param("date") LocalDateTime date) {
        return 0;
    }
}
