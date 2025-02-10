package com.ssafy.api.service;

import com.ssafy.api.response.GetTutorialRes;
import com.ssafy.db.entity.TutorialType;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserTutorialProgress;
import com.ssafy.db.repository.TutorialTypeRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserTutorialProgressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service("userTutorialProgressService")
public class UserTutorialProgressServiceImpl implements UserTutorialProgressService{

    private final UserTutorialProgressRepository tutorialProgressRepository;
    private final TutorialTypeRepository tutorialTypeRepository;

    @Autowired
    public UserTutorialProgressServiceImpl(UserTutorialProgressRepository tutorialProgressRepository, UserRepository userRepository, UserTutorialProgressRepository userTutorialProgressRepository, TutorialTypeRepository tutorialTypeRepository) {
        this.tutorialProgressRepository = tutorialProgressRepository;
        this.tutorialTypeRepository = tutorialTypeRepository;
    }

    @Override
    public List<GetTutorialRes> getTutorials(String userId) {
        List<UserTutorialProgress> tutorials = tutorialProgressRepository.findByUser_UserId(userId);
        return tutorials.stream()
                .map(GetTutorialRes::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTutorialCompleted(String userId, Long tutorialId) {
        TutorialType tutorialType = tutorialTypeRepository.findById(tutorialId).orElse(null);
        if (tutorialType == null) return false;

        return tutorialProgressRepository.existsByUser_UserIdAndTutorialTypeAndIsCompleted(userId, tutorialType, true);
    }

    @Transactional
    @Override
    public void completeTutorial(String userId, Long tutorialId) {
        TutorialType tutorialType = tutorialTypeRepository.findById(tutorialId).orElse(null);
        if (tutorialType == null) {
            throw new IllegalArgumentException("해당 튜토리얼이 존재하지 않습니다.");
        }

        UserTutorialProgress progress = tutorialProgressRepository.findByUser_UserIdAndTutorialType(userId, tutorialType);
        if (progress == null) {
            throw new IllegalArgumentException("튜토리얼 진행 데이터가 존재하지 않습니다.");
        }

        progress.setIsCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        tutorialProgressRepository.save(progress);
    }
}
