package com.ssafy.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public boolean sendUserIdEmail(String toEmail, String userId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("싸피트니스 : 회원님의 ID 찾기 결과");
            message.setText("안녕하세요,\n\n회원님의 User ID는 다음과 같습니다: " + userId + "\n\n감사합니다.");

            mailSender.send(message);  // 이메일 발송
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
