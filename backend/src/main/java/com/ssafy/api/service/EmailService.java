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

    public boolean sendUserPwEmail(String toEmail, String newPw) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("싸피트니스 : 회원님의 임시 비밀번호입니다.");
            message.setText("안녕하세요,\n\n회원님의 임시 비밀번호는 다음과 같습니다: " + newPw + "\n\n로그인 후 비밀번호를 꼭 변경하세요. \n\n감사합니다.");

            mailSender.send(message);  // 이메일 발송
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 회원가입 이메일 인증
    public boolean sendVerificationEmail(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("회원가입 이메일 인증코드 입니다.");
            message.setText("인증 코드 : " + verificationCode +
                    "\n\n4분 내에 입력해주세요!");

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
