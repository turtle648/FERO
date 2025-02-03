// accountAPI.js

import axios from 'axios';

const BASE_URL = 'http://i12e103.p.ssafy.io:8076/api/v1'

// 로그인 API 호출
export const login = async (id, password) => {
  try {
    const response = await axios.post(`${BASE_URL}/auth/login`, 
      { id, password }
    )
    console.log(response.data)
    return response.data['accessToken']
  } catch (error) {
    console.error(`로그인 요청 중 에러 발생: `, error)
    return false
  }
}


// 회원가입 API 호출 (세션 발급)
export const signUp = async (userId, password, userName, phoneNumber, userEmail) => {
  try {
    const response = await axios.post(`${BASE_URL}/users`, 
      { userId, password, userName, phoneNumber, userEmail }
    )
    console.log('회원가입 1단계 API 호출 결과:', response)
    return response.data
  } catch (error) {
    console.error(error)
    throw new Error('회원가입 실패')
  }
}


// 이메일 중복확인 API
export const checkEmailDuplicateAPI = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/users/check-email`, {
      params: { email }
    })
    if (response.data.statusCode === 200) {  
      console.log('이메일 중복 확인 결과:', response)
      return true
    }
  } catch (error) {
    alert('이메일 중복 확인 실패')
  }
}

// 이메일 인증확인 API
export const verifyEmail = async (code, email) => {
  try {
    // `code`와 `email`을 쿼리스트링으로 전달
    const response = await axios.post(
      `${BASE_URL}/users/verify-email?code=${code}&email=${encodeURIComponent(email)}`
    )
    console.log('이메일 인증 결과:', response)
    return response.data?.statusCode === 200
  } catch (error) {
    console.error('이메일 인증 실패:', error)
    alert('이메일 인증에 실패했습니다.')
    return false // 실패 시 false 반환
  }
}

// 캐릭터 등록 API 호출
export const registerCharacter = async (gender, sessionId) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/users/character?sessionId=${encodeURIComponent(sessionId)}`, 
      { gender }
    )
    console.log('캐릭터 등록 결과:', response);
    return response.data?.statusCode === 200;
  } catch (error) {
    console.error('캐릭터 등록 실패:', error);
    throw new Error('캐릭터 등록 실패');
  }
};

