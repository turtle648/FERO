package com.ssafy.api.service;

import com.ssafy.api.service.ChatMessageService;
import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.ChatRoom;
import com.ssafy.db.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.kurento.jsonrpc.client.JsonRpcClient.log;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage sendMessage(ChatRoom chatRoom, String senderId, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(senderId)
                .message(message)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getMessages(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoom(chatRoom);
    }

    // 매일 자정에 실행 - 3일 이상 지난 메시지 자동으로 삭제되게 설정
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldMessages() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        int deletedCount = chatMessageRepository.deleteBySentAtBefore(threeDaysAgo);
        log.info("3일 이상 지난 메시지 {}개 삭제 완료", deletedCount);
    }
}
