package com.ssafy.api.service;

import com.ssafy.api.request.UserUpdateReq;
import com.ssafy.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRepositorySupport userRepositorySupport;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setUserId(userRegisterInfo.getUserId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setUserPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setUserName(userRegisterInfo.getUserName());
		user.setUserEmail(userRegisterInfo.getUserEmail());
		user.setPhoneNumber(userRegisterInfo.getPhoneNumber());

		return userRepository.save(user);
	}

//	public UserCharacter createCharacter(UserCharacterRegisterPostReq userCharacterInfo){
//		UserCharacter usercharacter = new UserCharacter();
//
//		usercharacter.setUserNickname(userCharacterInfo.getUserNickname());
//		usercharacter.setGender(userCharacterInfo.getGender());
//
//		return userRepository.save(usercharacter);  // 올바른 레포지토리 사용
//	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepositorySupport.findUserByUserId(userId).get();
		System.out.println("user::" + user);
		return user;
	}

	@Override
	public User updateUser(UserUpdateReq updateInfo, String userId) {
		User user = getUserByUserId(userId);

		// 수정할 필드가 있으면 수정
		if (updateInfo.getUserEmail() != null && !updateInfo.getUserEmail().isEmpty()) {
			user.setUserEmail(updateInfo.getUserEmail());
		}

		if (updateInfo.getPhoneNumber() != null && !updateInfo.getPhoneNumber().isEmpty()) {
			user.setPhoneNumber(updateInfo.getPhoneNumber());
		}

		userRepository.save(user);  // user 수정

		return user;
	}


}
