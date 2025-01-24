// accountAPI.js

import axios from 'axios';

const BASE_URL = '/api/v1'; // API 기본 URL 설정


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


// 회원가입 API 호출 테스트용(세션 무조건 발급)
export const signUp = async () => {
    try {
      localStorage.setItem('sessionId', 1234)
      console.log("세션 Id 제대로 받아왔음")
    } catch (error) {
      throw new Error('회원가입 실패');
    }
};

// 회원가입 API 호출
// export const signUp = async (userData) => {
//         try {
//           const response = await axios.post(`${BASE_URL}/users`, userData)
//           localStorage.setItem('sessionId', response.data)
//           console.log("세션 Id 제대로 받아왔음")
//         } catch (error) {
//           throw new Error('회원가입 실패');
//         }
//     };


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

