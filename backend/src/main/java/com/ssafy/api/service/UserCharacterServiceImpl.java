package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.repository.UserCharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service("userCharacterService")
public class UserCharacterServiceImpl implements UserCharacterService {

    @Autowired
    UserCharacterRepository userCharacterRepository;

    public UserCharacter createUserCharacter(UserCharacterRegisterPostReq userCharacterRegisterInfo, User user) {
        UserCharacter usercharacter = new UserCharacter();

        usercharacter.setUserInfo(user);
        usercharacter.setUserNickname(userCharacterRegisterInfo.getUserNickname());
        usercharacter.setGender(userCharacterRegisterInfo.getGender());

        usercharacter.setPushupRecord((short) 0);
        usercharacter.setSquatRecord((short) 0);
        usercharacter.setPullupRecord((short) 0);
        usercharacter.setPoints((short) 0);

        return userCharacterRepository.save(usercharacter);
    }
}
