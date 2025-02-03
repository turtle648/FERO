// accountAPI.js

import axios from 'axios';

const BASE_URL = 'http://i12e103.p.ssafy.io:8076/api/v1'



// 로그인 API 호출
export const login = async (userId, password) => {
  try {
    const response = await axios.post(`${BASE_URL}/login`, {
      userId,
      password,
    });
    return response.data;
  } catch (error) {
    console.error(`로그인 요청 중 에러 발생: 입력받은 아이디 ${userId} 입력받은 비밀번호 ${password} `, error);
    throw error;
  }
};


// 회원가입 API 호출 (세션 발급)

export const signUp1 = async (userId, password, userName, phoneNumber, userEmail) => {
  try {
    const response = await axios.post(`${BASE_URL}/users`, {
      userId,
      password,
      userName,
      phoneNumber,
      userEmail,
    });
    // 요청 성공 시 처리
    return response.data; // 예시로 응답 데이터 반환
  } catch (error) {
    console.error(error);
    throw new Error('회원가입 실패');
  }
};


// 이메일 중복확인 API
export const checkEmailDuplicateAPI = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/users/check-email`, {
      params: { email }
    })
    if (response.data.statusCode === 200) {  
      console.log(response.data)
      return true
    }
  } catch (error) {
    alert('이메일 중복 확인 실패')
  }
};

// 캐릭터 등록 API 호출
export const registerCharacter = async (userId, gender, userNickname) => {
  const sessionId = localStorage.getItem('sessionId');  // localStorage에서 sessionId 가져오기
  // 세션 ID가 없으면 에러 처리
  if (!sessionId) {
    throw new Error('세션 ID가 존재하지 않습니다.');
  }
  try {
    const response = await axios.post(
      `${BASE_URL}/users/character?sessionId=${sessionId}`,
      {
        gender,
        userId,
        userNickname
      },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
    console.log('성공적으로 받음')
    return response.data;  // 응답 데이터 반환
  } catch (error) {
    console.error('API 호출 오류:', error);
    throw error;  // 에러 발생 시 오류를 던짐
  }
};

